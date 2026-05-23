package com.example.springboot.portfolio.portfolio_asset;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioAssetMeanPriceRepository
    extends JpaRepository<PortfolioAssetMeanPrice, Long> {
  Optional<PortfolioAssetMeanPrice> findByPortfolioAssetId(Long id);
}
