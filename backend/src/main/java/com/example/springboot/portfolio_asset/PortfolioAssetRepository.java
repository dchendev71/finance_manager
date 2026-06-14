package com.example.springboot.portfolio_asset;

import com.example.springboot.asset.Asset;
import com.example.springboot.common.exception.NotFoundException;
import com.example.springboot.portfolio.Portfolio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

  @Modifying
  @Query("DELETE FROM PortfolioAsset pa WHERE pa.id = :id")
  void deleteDirectlyById(@Param("id") Long id);
}
