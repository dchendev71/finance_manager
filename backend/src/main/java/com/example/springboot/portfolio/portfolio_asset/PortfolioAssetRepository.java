package com.example.springboot.portfolio.portfolio_asset;

import com.example.springboot.common.exception.NotFoundException;
import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.asset.Asset;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioAssetRepository extends JpaRepository<PortfolioAsset, Long> {
  Optional<PortfolioAsset> findByAssetNameAndPortfolioId(String assetName, Long portfolioId);

  List<PortfolioAsset> findAllByPortfolioId(Long id);

  default PortfolioAsset getByPortfolioAndAsset(Portfolio portfolio, Asset asset) {

    PortfolioAsset portfolioAsset =
        this.findByAssetNameAndPortfolioId(asset.getName(), portfolio.getId())
            .orElseThrow(
                () ->
                    new NotFoundException(
                        PortfolioAsset.class, portfolio.getName() + ":" + asset.getName()));
    return portfolioAsset;
  }
}
