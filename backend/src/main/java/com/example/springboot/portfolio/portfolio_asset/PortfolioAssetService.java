package com.example.springboot.portfolio.portfolio_asset;

import com.example.springboot.common.exception.ExistsException;
import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.PortfolioRepository;
import com.example.springboot.portfolio.asset.Asset;
import com.example.springboot.portfolio.asset.AssetRepository;
import com.example.springboot.portfolio.portfolio_asset.dto.PortfolioAssetRequest;
import com.example.springboot.portfolio.portfolio_asset.dto.PortfolioAssetResponse;
import com.example.springboot.portfolio.portfolio_asset.mapper.PortfolioAssetMapper;
import com.example.springboot.user.User;
import com.example.springboot.user.UserRepository;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioAssetService {
  private final PortfolioAssetMeanPriceService portfolioAssetMeanPriceService;
  // Repository for service use
  private final PortfolioAssetRepository portfolioAssetRepository;
  private final UserRepository userRepository;
  private final PortfolioRepository portfolioRepository;
  private final AssetRepository assetRepository;

  // Mapper
  private final PortfolioAssetMapper portfolioAssetMapper;

  private Portfolio checkUserAndPortfolioExists(String email, String portfolioName) {

    User user = userRepository.getByEmailOrThrow(email);
    // Check if portfolio associated with the user exist
    Portfolio portfolio =
        portfolioRepository.getByUserIdAndNameOrThrow(user.getId(), portfolioName);

    return portfolio;
  }

  private Asset checkAssetExists(String assetName) {
    return assetRepository.getByNameOrThrow(assetName);
  }

  public PortfolioAssetResponse createPortfolioAsset(String email, PortfolioAssetRequest request) {
    Portfolio portfolio = checkUserAndPortfolioExists(email, request.portfolioName());
    Asset asset = checkAssetExists(request.assetName());
    // Check if portfolioAsset already exists - ie if we want to add quantity then use update
    if (portfolioAssetRepository
        .findByAssetNameAndPortfolioId(request.assetName(), portfolio.getId())
        .isPresent()) {
      throw new ExistsException(PortfolioAsset.class, request.assetName());
    }

    PortfolioAsset portfolioAsset =
        portfolioAssetRepository.save(
            portfolioAssetMapper.toEntity(portfolio, asset, request.quantity()));

    // Mean price tracker
    portfolioAssetMeanPriceService.createMeanPrice(portfolioAsset, request.price());
    // Record Transaction
    return portfolioAssetMapper.toResponse(portfolioAsset);
  }

  public Optional<PortfolioAssetResponse> updatePortfolioAsset(
      String email, PortfolioAssetRequest request) {

    Portfolio portfolio = checkUserAndPortfolioExists(email, request.portfolioName());
    Asset asset = checkAssetExists(request.assetName());

    PortfolioAsset portfolioAsset =
        portfolioAssetRepository.getByPortfolioAndAsset(portfolio, asset);

    // We update the mean price as if quantity is < 0, is does nothing
    portfolioAssetMeanPriceService.updateMeanPrice(
        portfolioAsset, request.quantity(), request.price());

    // We update the PortfolioAsset
    BigDecimal newQuantity = portfolioAsset.getQuantity().add(request.quantity());
    if (newQuantity.compareTo(BigDecimal.ZERO) >= 0) {
      portfolioAsset.setQuantity(newQuantity);
      return Optional.of(
          portfolioAssetMapper.toResponse(portfolioAssetRepository.save(portfolioAsset)));
    }
    // Delete the row as every position has been sold
    portfolioAssetRepository.delete(portfolioAsset);
    return Optional.empty();
  }
}
