package com.example.springboot.portfolio_asset;

import com.example.springboot.asset.Asset;
import com.example.springboot.asset.AssetRepository;
import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.PortfolioRepository;
import com.example.springboot.portfolio_asset.mean_price.PortfolioAssetMeanPrice;
import com.example.springboot.portfolio_asset.mean_price.PortfolioAssetMeanPriceService;
import com.example.springboot.user.User;
import com.example.springboot.user.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataService {
  private final UserRepository userRepository;
  private final PortfolioRepository portfolioRepository;
  private final AssetRepository assetRepository;
  private final PortfolioAssetRepository portfolioAssetRepository;
  private final PortfolioAssetMeanPriceService portfolioAssetMeanPriceService;

  public record Data(
      User user,
      Asset asset,
      Portfolio portfolio,
      Optional<PortfolioAsset> portfolioAsset,
      Optional<PortfolioAssetMeanPrice> meanPrice) {}

  public Data fetchData(String email, String portfolioName, String assetName) {
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

  public Data updateData(
      Data data,
      Optional<PortfolioAsset> portfolioAsset,
      Optional<PortfolioAssetMeanPrice> meanPrice) {

    return new Data(data.user(), data.asset(), data.portfolio(), portfolioAsset, meanPrice);
  }
}
