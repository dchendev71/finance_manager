package com.example.springboot.portfolio.portfolio_asset;

import com.example.springboot.common.exception.ExistsException;
import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.PortfolioRepository;
import com.example.springboot.portfolio.asset.Asset;
import com.example.springboot.portfolio.asset.AssetRepository;
import com.example.springboot.portfolio.portfolio_asset.dto.CreatePortfolioAssetRequest;
import com.example.springboot.portfolio.portfolio_asset.dto.PortfolioAssetResponse;
import com.example.springboot.portfolio.portfolio_asset.mapper.PortfolioAssetMapper;
import com.example.springboot.user.User;
import com.example.springboot.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioAssetService {
  private final PortfolioAssetRepository portfolioAssetRepository;
  private final UserRepository userRepository;
  private final PortfolioRepository portfolioRepository;
  private final AssetRepository assetRepository;
  private final PortfolioAssetMapper portfolioAssetMapper;

  public PortfolioAssetResponse createPortfolioAsset(
      String email, CreatePortfolioAssetRequest request) {
    // Check if user still exists
    User user = userRepository.getByEmailOrThrow(email);
    // Check if portfolio associated with the user exist
    Portfolio portfolio =
        portfolioRepository.getByUserIdAndNameOrThrow(user.getId(), request.portfolioName());
    // Check if Asset exists
    Asset asset = assetRepository.getByNameOrThrow(request.assetName());
    // Check if portfolioAsset already exists - ie if we want to add quantity then use ADD
    // endpoint
    if (portfolioAssetRepository
        .findByAssetNameAndPortfolioId(request.assetName(), portfolio.getId())
        .isPresent()) {
      throw new ExistsException(PortfolioAsset.class, request.assetName());
    }

    PortfolioAsset portfolioAsset =
        portfolioAssetRepository.save(
            portfolioAssetMapper.toEntity(portfolio, asset, request.quantity()));

    return portfolioAssetMapper.toResponse(portfolioAsset);
  }
}
