package com.example.springboot.portfolio_asset;

import com.example.springboot.asset.AssetRepository;
import com.example.springboot.common.exception.ExistsException;
import com.example.springboot.common.exception.InsufficientBalanceException;
import com.example.springboot.common.exception.NotFoundException;
import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.PortfolioRepository;
import com.example.springboot.portfolio_asset.BalanceCheckerService.CheckedValues;
import com.example.springboot.portfolio_asset.DataService.Data;
import com.example.springboot.portfolio_asset.dto.PortfolioAssetRequest;
import com.example.springboot.portfolio_asset.dto.PortfolioAssetResponse;
import com.example.springboot.portfolio_asset.mapper.PortfolioAssetMapper;
import com.example.springboot.portfolio_asset.mean_price.PortfolioAssetMeanPrice;
import com.example.springboot.portfolio_asset.mean_price.PortfolioAssetMeanPriceService;
import com.example.springboot.recorder.RecordService;
import com.example.springboot.user.User;
import com.example.springboot.user.UserRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioAssetService {
  // Helper service
  private final BalanceCheckerService balanceCheckerService;
  private final DataService dataService;
  private final RecordService recordService;
  // Repository for service use
  private final PortfolioAssetMeanPriceService portfolioAssetMeanPriceService;
  private final PortfolioAssetRepository portfolioAssetRepository;
  private final UserRepository userRepository;
  private final PortfolioRepository portfolioRepository;
  private final AssetRepository assetRepository;
  // Mapper
  private final PortfolioAssetMapper portfolioAssetMapper;

  // TODO: Write test
  public List<PortfolioAssetResponse> getPortfolioAssets(String email, String portfolioName) {
    User user = userRepository.getByEmailOrThrow(email);
    Portfolio portfolio =
        portfolioRepository.getByUserIdAndNameOrThrow(user.getId(), portfolioName);
    // We find all assets from this current portfolio, fetch the meanPrice for each, and return
    // As an response
    return portfolioAssetRepository.findAllByPortfolioId(portfolio.getId()).stream()
        .map(
            portfolioAsset -> {
              Optional<PortfolioAssetMeanPrice> meanPrice =
                  portfolioAssetMeanPriceService.getMeanPrice(portfolioAsset);
              if (meanPrice.isEmpty()) {
                throw new NotFoundException(
                    PortfolioAssetMeanPrice.class, portfolioAsset.getAsset().getName());
              }
              return portfolioAssetMapper.toResponse(
                  portfolioAsset, meanPrice.get().getMeanPrice());
            })
        .toList();
  }

  private PortfolioAsset createAssetAndWriteRecords(Data data, PortfolioAssetRequest request) {
    CheckedValues check = balanceCheckerService.performCheck(data.user(), request);
    if (!check.ok()) {
      throw new InsufficientBalanceException();
    }
    PortfolioAsset portfolioAsset =
        portfolioAssetRepository.save(
            portfolioAssetMapper.toEntity(data.portfolio(), data.asset(), request.quantity()));

    Data newData = dataService.updateData(data, Optional.of(portfolioAsset), Optional.empty());

    recordService.writeRecords(
        newData, check.accurateQty(), check.assetCurrentPrice(), check.remainingBalance());

    return portfolioAsset;
  }

  @Transactional
  public PortfolioAssetResponse createPortfolioAsset(
      String email, String portfolioName, PortfolioAssetRequest request) {
    Data data = dataService.fetchData(email, portfolioName, request.assetName());
    // Check if portfolioAsset already exists - ie if we want to add quantity then use update
    if (data.portfolioAsset().isPresent()) {
      throw new ExistsException(PortfolioAsset.class, request.assetName());
    }
    PortfolioAsset portfolioAsset = createAssetAndWriteRecords(data, request);
    // To Response
    PortfolioAssetMeanPrice meanPrice =
        portfolioAssetMeanPriceService.getMeanPriceOrThrow(portfolioAsset);
    return portfolioAssetMapper.toResponse(portfolioAsset, meanPrice.getMeanPrice());
  }

  @Transactional
  public Optional<PortfolioAssetResponse> updatePortfolioAsset(
      String email, String portfolioName, PortfolioAssetRequest request) {
    Data data = dataService.fetchData(email, portfolioName, request.assetName());
    // If not found, should use the create endpoint
    if (data.portfolioAsset().isEmpty()) {
      throw new NotFoundException(PortfolioAsset.class, "asset not exists");
    }
    PortfolioAsset portfolioAsset = data.portfolioAsset().get();
    CheckedValues check = balanceCheckerService.performCheck(data.user(), request, portfolioAsset);
    if (!check.ok()) {
      throw new InsufficientBalanceException();
    }
    // Record transaction
    recordService.writeRecords(
        data, check.accurateQty(), check.assetCurrentPrice(), check.remainingBalance());
    BigDecimal newQuantity = check.accurateQty().add(portfolioAsset.getQuantity());

    // Check if the request leads to a Zero Balance so we need to delete in repository
    if (newQuantity.compareTo(BigDecimal.ZERO) == 0) {
      // Delete the row as every position has been sold
      portfolioAssetRepository.deleteDirectlyById(portfolioAsset.getId());
      return Optional.empty();
    }
    // Otherwise update to the new quantity
    portfolioAsset.setQuantity(newQuantity);
    PortfolioAssetMeanPrice meanPrice =
        portfolioAssetMeanPriceService.getMeanPriceOrThrow(portfolioAsset);

    return Optional.of(
        portfolioAssetMapper.toResponse(
            portfolioAssetRepository.save(portfolioAsset), meanPrice.getMeanPrice()));
  }

  @Transactional
  public void deletePortfolioAsset(String email, String portfolioName, String assetName) {
    // Create a fake request to use utility function

    Data data = dataService.fetchData(email, portfolioName, assetName);

    if (data.portfolioAsset().isEmpty()) {
      return;
    }

    portfolioAssetRepository.deleteDirectlyById(data.portfolioAsset().get().getId());
  }
}
