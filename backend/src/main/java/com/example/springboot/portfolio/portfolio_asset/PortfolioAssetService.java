package com.example.springboot.portfolio.portfolio_asset;

import com.example.springboot.common.exception.ExistsException;
import com.example.springboot.common.exception.NotFoundException;
import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.PortfolioRepository;
import com.example.springboot.portfolio.asset.Asset;
import com.example.springboot.portfolio.asset.AssetRepository;
import com.example.springboot.portfolio.portfolio_asset.dto.PortfolioAssetRequest;
import com.example.springboot.portfolio.portfolio_asset.dto.PortfolioAssetResponse;
import com.example.springboot.portfolio.portfolio_asset.mapper.PortfolioAssetMapper;
import com.example.springboot.portfolio.portfolio_asset.mean_price.PortfolioAssetMeanPrice;
import com.example.springboot.portfolio.portfolio_asset.mean_price.PortfolioAssetMeanPriceService;
import com.example.springboot.portfolio.transactions.TransactionsService;
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
  private final PortfolioAssetMeanPriceService portfolioAssetMeanPriceService;
  private final TransactionsService transactionService;
  // Repository for service use
  private final PortfolioAssetRepository portfolioAssetRepository;
  private final UserRepository userRepository;
  private final PortfolioRepository portfolioRepository;
  private final AssetRepository assetRepository;
  // Mapper
  private final PortfolioAssetMapper portfolioAssetMapper;

  private record Data(
      User user,
      Asset asset,
      Portfolio portfolio,
      Optional<PortfolioAsset> portfolioAsset,
      Optional<PortfolioAssetMeanPrice> meanPrice) {}

  private Data fetchData(String email, String portfolioName, String assetName) {
    User user = userRepository.getByEmailOrThrow(email);
    Portfolio portfolio =
        portfolioRepository.getByUserIdAndNameOrThrow(user.getId(), portfolioName);
    Asset asset = assetRepository.getByNameOrThrow(assetName);
    Optional<PortfolioAsset> portfolioAsset =
        portfolioAssetRepository.findByAssetNameAndPortfolioId(assetName, portfolio.getId());
    if (portfolioAsset.isEmpty()) {
      return new Data(user, asset, portfolio, Optional.empty(), Optional.empty());
    }
    Optional<PortfolioAssetMeanPrice> meanPrice =
        portfolioAssetMeanPriceService.getMeanPrice(portfolioAsset.get());

    return new Data(user, asset, portfolio, portfolioAsset, meanPrice);
  }

  private Data updateData(
      Data data,
      Optional<PortfolioAsset> portfolioAsset,
      Optional<PortfolioAssetMeanPrice> meanPrice) {

    return new Data(data.user(), data.asset(), data.portfolio(), portfolioAsset, meanPrice);
  }

  private void updateRecords(Data dataRecord, BigDecimal quantity, BigDecimal unitPrice) {
    portfolioAssetMeanPriceService.updateMeanPrice(
        dataRecord.portfolioAsset().get(), quantity, unitPrice);
    transactionService.recordTransaction(
        dataRecord.user(), dataRecord.asset(), quantity, unitPrice);
  }

  private BigDecimal getAccurateRequestQuantity(
      PortfolioAsset portfolioAsset, BigDecimal quantity) {
    BigDecimal newQty = portfolioAsset.getQuantity().add(quantity);
    if (newQty.compareTo(BigDecimal.ZERO) < 0) {
      // This prevent an user from 'overselling' i.e can only sell at most the quantity
      return portfolioAsset.getQuantity().negate();
    }
    return quantity;
  }

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

  @Transactional
  public PortfolioAssetResponse createPortfolioAsset(
      String email, String portfolioName, PortfolioAssetRequest request) {
    Data data = fetchData(email, portfolioName, request.assetName());
    // Check if portfolioAsset already exists - ie if we want to add quantity then use update
    if (data.portfolioAsset().isPresent()) {
      throw new ExistsException(PortfolioAsset.class, request.assetName());
    }

    // Create and save entity
    PortfolioAsset portfolioAsset =
        portfolioAssetRepository.save(
            portfolioAssetMapper.toEntity(data.portfolio(), data.asset(), request.quantity()));
    // TODO: Refacto the code below, we are breaking SRP

    // Create new record with updated value
    Data newData = updateData(data, Optional.of(portfolioAsset), Optional.empty());
    // This function will create the MeanPrice value inside the DB
    updateRecords(newData, request.quantity(), request.unitPrice());
    // To Response
    PortfolioAssetMeanPrice meanPrice =
        portfolioAssetMeanPriceService.getMeanPriceOrThrow(portfolioAsset);
    return portfolioAssetMapper.toResponse(portfolioAsset, meanPrice.getMeanPrice());
  }

  @Transactional
  public Optional<PortfolioAssetResponse> updatePortfolioAsset(
      String email, String portfolioName, PortfolioAssetRequest request) {
    Data data = fetchData(email, portfolioName, request.assetName());
    // If not found, should use the create endpoint
    if (data.portfolioAsset().isEmpty()) {
      throw new NotFoundException(PortfolioAsset.class, "asset not exists");
    }
    PortfolioAsset portfolioAsset = data.portfolioAsset().get();

    // We need to check if the request has a negative quantity that exceeds our quantity or not
    BigDecimal accurateRequestQty = getAccurateRequestQuantity(portfolioAsset, request.quantity());
    // We update the PortfolioAsset
    BigDecimal newQuantity = portfolioAsset.getQuantity().add(accurateRequestQty);
    if (newQuantity.compareTo(BigDecimal.ZERO) <= 0) {
      updateRecords(data, newQuantity, request.unitPrice());

      // Delete the row as every position has been sold
      portfolioAssetRepository.deleteDirectlyById(portfolioAsset.getId());
      return Optional.empty();
    }

    updateRecords(data, accurateRequestQty, request.unitPrice());

    // We set the quantity after updateRecords because it is going to use the PortfolioAssetQuantity
    // as argument
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

    Data data = fetchData(email, portfolioName, assetName);

    if (data.portfolioAsset().isEmpty()) {
      return;
    }

    portfolioAssetRepository.deleteDirectlyById(data.portfolioAsset().get().getId());
  }
}
