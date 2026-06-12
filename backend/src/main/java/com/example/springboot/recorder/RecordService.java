package com.example.springboot.recorder;

import com.example.springboot.balance.UserBalanceService;
import com.example.springboot.portfolio.portfolio_asset.PortfolioAssetService;
import com.example.springboot.portfolio.portfolio_asset.mean_price.PortfolioAssetMeanPriceService;
import com.example.springboot.portfolio.transactions.TransactionsService;
import com.example.springboot.user.User;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordService {
  private final PortfolioAssetMeanPriceService portfolioAssetMeanPriceService;
  private final TransactionsService transactionService;
  private final UserBalanceService userBalanceService;

  private void updateNewBalance(User user, BigDecimal newBalance) {
    userBalanceService.updateBalance(user, newBalance);
  }

  @Transactional
  public void writeRecords(
      PortfolioAssetService.Data dataRecord,
      BigDecimal quantity,
      BigDecimal unitPrice,
      BigDecimal newBalance) {
    portfolioAssetMeanPriceService.updateMeanPrice(
        dataRecord.portfolioAsset().get(), quantity, unitPrice);
    transactionService.recordTransaction(
        dataRecord.user(), dataRecord.asset(), quantity, unitPrice);
    updateNewBalance(dataRecord.user(), newBalance);
  }
}
