package com.example.springboot.portfolio.transactions;

import com.example.springboot.portfolio.asset.Asset;
import com.example.springboot.user.User;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionsService {
  private final TransactionsRepository transactionsRepository;

  public void recordTransaction(User user, Asset asset, BigDecimal quantity, BigDecimal unitPrice) {
    Transactions transactions =
        Transactions.builder()
            .quantity(quantity)
            .unitPrice(unitPrice)
            .user(user)
            .asset(asset)
            .build();
    transactionsRepository.save(transactions);
  }
}
