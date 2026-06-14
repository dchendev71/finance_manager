package com.example.springboot.transactions;

import com.example.springboot.asset.Asset;
import com.example.springboot.user.User;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionsService {
  private final TransactionsRepository transactionsRepository;

  @Transactional
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
