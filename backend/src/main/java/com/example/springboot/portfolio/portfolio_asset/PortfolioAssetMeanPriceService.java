package com.example.springboot.portfolio.portfolio_asset;

import com.example.springboot.common.exception.NotFoundException;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioAssetMeanPriceService {
  private final PortfolioAssetMeanPriceRepository portfolioAssetMeanPriceRepository;

  public void createMeanPrice(PortfolioAsset portfolioAsset, BigDecimal price) {
    PortfolioAssetMeanPrice meanPrice =
        PortfolioAssetMeanPrice.builder().meanPrice(price).portfolioAsset(portfolioAsset).build();
    portfolioAssetMeanPriceRepository.save(meanPrice);
  }

  // Note: When selling, the mean price acquired of an asset is the same
  public void updateMeanPrice(
      PortfolioAsset portfolioAsset, BigDecimal quantity, BigDecimal price) {
    if (quantity.compareTo(BigDecimal.ZERO) < 0) {
      return;
    }
    // Get our current mean price
    PortfolioAssetMeanPrice meanPrice =
        portfolioAssetMeanPriceRepository
            .findByPortfolioAssetId(portfolioAsset.getId())
            .orElseThrow(() -> new NotFoundException(PortfolioAsset.class, "portfolioAsset row"));

    BigDecimal totalPrice =
        meanPrice
            .getMeanPrice()
            .multiply(portfolioAsset.getQuantity())
            .add(price.multiply(quantity));

    BigDecimal totalQty = portfolioAsset.getQuantity().add(quantity);

    BigDecimal newMeanPrice = totalPrice.divide(totalQty);

    meanPrice.setMeanPrice(newMeanPrice);

    portfolioAssetMeanPriceRepository.save(meanPrice);
  }
}
