package com.example.springboot.portfolio.portfolio_asset.mean_price;

import com.example.springboot.common.exception.NotFoundException;
import com.example.springboot.portfolio.portfolio_asset.PortfolioAsset;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

  public Optional<PortfolioAssetMeanPrice> getMeanPrice(PortfolioAsset portfolioAsset) {
    return portfolioAssetMeanPriceRepository.findByPortfolioAssetId(portfolioAsset.getId());
  }

  public PortfolioAssetMeanPrice getMeanPriceOrThrow(PortfolioAsset portfolioAsset) {
    Optional<PortfolioAssetMeanPrice> meanPrice = this.getMeanPrice(portfolioAsset);
    if (meanPrice.isEmpty()) {
      throw new NotFoundException(
          PortfolioAssetMeanPrice.class, portfolioAsset.getAsset().getName());
    }
    return meanPrice.get();
  }

  // Note: When selling, the mean price acquired of an asset is the same
  @Transactional
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

    BigDecimal newMeanPrice = totalPrice.divide(totalQty, 8, RoundingMode.HALF_DOWN);

    meanPrice.get().setMeanPrice(newMeanPrice);

    portfolioAssetMeanPriceRepository.save(meanPrice.get());
  }
}
