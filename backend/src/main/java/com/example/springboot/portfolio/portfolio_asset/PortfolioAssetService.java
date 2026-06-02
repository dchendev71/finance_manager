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
import com.example.springboot.portfolio.portfolio_asset.mean_price.PortfolioAssetMeanPriceService;
import com.example.springboot.portfolio.transactions.TransactionsService;
import com.example.springboot.user.User;
import com.example.springboot.user.UserRepository;
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

  private record DataHolder(
      User user, Asset asset, Portfolio portfolio, Optional<PortfolioAsset> portfolioAsset) {}

  private DataHolder createDataHolder(
      String email, String portfolioName, PortfolioAssetRequest request) {
    User user = userRepository.getByEmailOrThrow(email);
    Portfolio portfolio =
        portfolioRepository.getByUserIdAndNameOrThrow(user.getId(), portfolioName);
    Asset asset = assetRepository.getByNameOrThrow(request.assetName());
    Optional<PortfolioAsset> portfolioAsset =
        portfolioAssetRepository.findByAssetNameAndPortfolioId(
            request.assetName(), portfolio.getId());
    return new DataHolder(user, asset, portfolio, portfolioAsset);
  }

  private void updateRecords(DataHolder dataHolder, BigDecimal quantity, BigDecimal unitPrice) {
    portfolioAssetMeanPriceService.updateMeanPrice(
        dataHolder.portfolioAsset().get(), quantity, unitPrice);
    transactionService.recordTransaction(
        dataHolder.user(), dataHolder.asset(), quantity, unitPrice);
  }

  private BigDecimal getAccurateRequestQuantity(
      PortfolioAsset portfolioAsset, BigDecimal quantity) {
    if (quantity.compareTo(BigDecimal.ZERO) < 0) {
      return portfolioAsset.getQuantity();
    }
    return quantity;
  }

  // TODO: Write test
  public List<PortfolioAssetResponse> getPortfolioAssets(String email, String portfolioName) {
    User user = userRepository.getByEmailOrThrow(email);
    Portfolio portfolio =
        portfolioRepository.getByUserIdAndNameOrThrow(user.getId(), portfolioName);

    return portfolioAssetRepository.findAllByPortfolioId(portfolio.getId()).stream()
        .map(portfolioAssetMapper::toResponse)
        .toList();
  }

  public PortfolioAssetResponse createPortfolioAsset(
      String email, String portfolioName, PortfolioAssetRequest request) {
    DataHolder dataHolder = createDataHolder(email, portfolioName, request);
    // Check if portfolioAsset already exists - ie if we want to add quantity then use update
    if (dataHolder.portfolioAsset().isPresent()) {
      throw new ExistsException(PortfolioAsset.class, request.assetName());
    }

    // Create and save entity
    PortfolioAsset portfolioAsset =
        portfolioAssetRepository.save(
            portfolioAssetMapper.toEntity(
                dataHolder.portfolio(), dataHolder.asset(), request.quantity()));

    // Update Mean price and transactions, yes, we create a new data holder because of new
    // portfolioAsset
    updateRecords(
        new DataHolder(
            dataHolder.user(),
            dataHolder.asset(),
            dataHolder.portfolio(),
            Optional.of(portfolioAsset)),
        request.quantity(),
        request.unitPrice());
    // To Response
    return portfolioAssetMapper.toResponse(portfolioAsset);
  }

  public Optional<PortfolioAssetResponse> updatePortfolioAsset(
      String email, String portfolioName, PortfolioAssetRequest request) {
    DataHolder dataHolder = createDataHolder(email, portfolioName, request);
    // If not found, should use the create endpoint
    if (dataHolder.portfolioAsset().isEmpty()) {
      throw new NotFoundException(PortfolioAsset.class, "");
    }
    PortfolioAsset portfolioAsset = dataHolder.portfolioAsset().get();
    // We need to check if the request has a negative quantity that exceeds our quantity or not
    BigDecimal accurateRequestQty = getAccurateRequestQuantity(portfolioAsset, request.quantity());
    // We update the PortfolioAsset
    BigDecimal newQuantity = portfolioAsset.getQuantity().add(accurateRequestQty);
    if (newQuantity.compareTo(BigDecimal.ZERO) <= 0) {
      // Delete the row as every position has been sold
      portfolioAssetRepository.delete(portfolioAsset);
      updateRecords(dataHolder, accurateRequestQty, request.unitPrice());
      return Optional.empty();
    }

    portfolioAsset.setQuantity(newQuantity);
    updateRecords(dataHolder, accurateRequestQty, request.unitPrice());
    return Optional.of(
        portfolioAssetMapper.toResponse(portfolioAssetRepository.save(portfolioAsset)));
  }

  public void deletePortfolioAsset(String email, String portfolioName, String assetName) {
    // Create a fake request to use utility function
    DataHolder dataHolder =
        createDataHolder(
            email,
            portfolioName,
            new PortfolioAssetRequest(assetName, BigDecimal.ZERO, BigDecimal.ZERO));

    if (dataHolder.portfolioAsset().isEmpty()) {
      return;
    }

    portfolioAssetRepository.delete(dataHolder.portfolioAsset().get());
  }
}
