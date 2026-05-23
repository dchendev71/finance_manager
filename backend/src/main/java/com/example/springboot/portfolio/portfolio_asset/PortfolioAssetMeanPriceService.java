package com.example.springboot.portfolio.portfolio_asset;

import java.math.BigDecimal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioAssetMeanPriceService {
  private final PortfolioAssetMeanPriceRepository portfolioAssetMeanPriceRepository;

  private void createMeanPrice(PortfolioAsset portfolioAsset, BigDecimal price) {
    PortfolioAssetMeanPrice meanPrice =
        PortfolioAssetMeanPrice.builder().meanPrice(price).portfolioAsset(portfolioAsset).build();
    portfolioAssetMeanPriceRepository.save(meanPrice);
  }

  // Note: When selling, the mean price acquired of an asset is the same
  public void updateMeanPrice(
      PortfolioAsset portfolioAsset, BigDecimal quantity, BigDecimal unitPrice) {
    if (quantity.compareTo(BigDecimal.ZERO) < 0) {
      return;
    }
    // Get our current mean price
    Optional<PortfolioAssetMeanPrice> meanPrice =
        portfolioAssetMeanPriceRepository.findByPortfolioAssetId(portfolioAsset.getId());
    // If no mean price, first mean price, create it
    if (meanPrice.isEmpty()) {
      createMeanPrice(portfolioAsset, unitPrice);
      return;
    }

    BigDecimal totalPrice =
        meanPrice
            .get()
            .getMeanPrice()
            .multiply(portfolioAsset.getQuantity())
            .add(unitPrice.multiply(quantity));

    BigDecimal totalQty = portfolioAsset.getQuantity().add(quantity);

    BigDecimal newMeanPrice = totalPrice.divide(totalQty);

    meanPrice.get().setMeanPrice(newMeanPrice);

    portfolioAssetMeanPriceRepository.save(meanPrice.get());
  }
}
