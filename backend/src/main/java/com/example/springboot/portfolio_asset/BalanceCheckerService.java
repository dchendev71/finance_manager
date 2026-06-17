package com.example.springboot.portfolio_asset;

import com.example.springboot.asset.Asset;
import com.example.springboot.asset.AssetRepository;
import com.example.springboot.balance.UserBalanceService;
import com.example.springboot.common.exception.NotFoundException;
import com.example.springboot.portfolio_asset.dto.PortfolioAssetRequest;
import com.example.springboot.price.PriceService;
import com.example.springboot.user.User;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceCheckerService {
  private final PriceService priceService;
  private final UserBalanceService userBalanceService;
  private final AssetRepository assetRepository;

  private BigDecimal getAccurateRequestQuantity(
      PortfolioAsset portfolioAsset, BigDecimal quantity) {
    BigDecimal newQty = portfolioAsset.getQuantity().add(quantity);
    if (newQty.compareTo(BigDecimal.ZERO) < 0) {
      // This prevent an user from 'overselling' i.e can only sell at most the quantity
      return portfolioAsset.getQuantity().negate();
    }
    return quantity;
  }

  public record CheckedValues(
      Boolean ok,
      BigDecimal remainingBalance,
      BigDecimal accurateQty,
      BigDecimal assetCurrentPrice) {}

  private Boolean checkSlippage(BigDecimal current, BigDecimal expected, BigDecimal maxSlippage) {
    if (expected.compareTo(BigDecimal.ZERO) == 0) {
      throw new ArithmeticException("Expected price cannot be zero");
    }
    BigDecimal slippage =
        current.subtract(expected).divide(expected, 10, RoundingMode.HALF_UP).abs();

    return (slippage.compareTo(maxSlippage) <= 0);
  }

  public CheckedValues performCheck(
      User user, PortfolioAssetRequest request, PortfolioAsset portfolioAsset) {

    // This will lead to ceil(request.quantity) to the max qty we have in portfolioAsset (on
    // negative value)
    BigDecimal accurateRequestQty = getAccurateRequestQuantity(portfolioAsset, request.quantity());
    return performCheck(
        user,
        new PortfolioAssetRequest(request.assetName(), accurateRequestQty, request.unitPrice()));
  }

  public CheckedValues performCheck(User user, PortfolioAssetRequest request) {
    // Check if unitPrice is valid i.e >= 0
    if (request.unitPrice().compareTo(BigDecimal.ZERO) <= 0) {
      return new CheckedValues(false, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }
    // Check slippage
    Optional<Asset> asset = assetRepository.findByName(request.assetName());
    if (asset.isEmpty()) {
      throw new NotFoundException(Asset.class, request.assetName());
    }

    BigDecimal currentPrice =
        new BigDecimal(priceService.getIndicativePrice(asset.get().getTickerSymbol()));

    // TODO: Slippage 2%, create new Error
    if (!checkSlippage(currentPrice, request.unitPrice(), new BigDecimal(0.02))) {
      throw new ArithmeticException("Max slippage");
    }
    // Now check balance
    BigDecimal balance = userBalanceService.getBalance(user.getEmail()).getBalance();

    /* If quantity is > 0, standard buy, check if balance is enough
     * If quantity is < 0, the totalCost would be negative, meaning that OK is still true
     * If quantity is < 0, the 'remainingBalance' from Checkedvalues would increase as we subtract (negative)
     */
    // We use currentPrice which is data fetched from DB - redis
    BigDecimal totalCost = request.quantity().multiply(currentPrice);
    Boolean ok = balance.compareTo(totalCost) >= 0;

    return new CheckedValues(ok, balance.subtract(totalCost), request.quantity(), currentPrice);
  }
}
