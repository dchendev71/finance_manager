package com.example.springboot.portfolio.portfolio_asset;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioAssetRepository extends JpaRepository<PortfolioAsset, Long> {
  Optional<PortfolioAsset> findByAssetNameAndPortfolioId(String assetName, Long portfolioId);
}
