package com.example.springboot.balance;

import com.example.springboot.balance.dto.UserBalanceRequest;
import com.example.springboot.user.User;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserBalanceService {
  private final UserBalanceRepository userBalanceRepository;

  public void createInitialBalance(User user) {
    UserBalance userBalance = UserBalance.builder().user(user).balance(BigDecimal.ZERO).build();
    userBalanceRepository.save(userBalance);
  }

  public void increaseBalance(String email, UserBalanceRequest request) {
    UserBalance userBalance = userBalanceRepository.getByUserEmailOrThrow(email);

    BigDecimal newAmount = userBalance.getBalance().add(request.increaseAmount());
    userBalance.setBalance(newAmount);

    userBalanceRepository.save(userBalance);
  }

  public BigDecimal getUserCurrentBalance(String email) {
    return userBalanceRepository.getByUserEmailOrThrow(email).getBalance();
  }

  public Boolean isUserBalanceEnough(String email, BigDecimal amount) {
    BigDecimal balance = this.getUserCurrentBalance(email);
    // This mean that balance is greater or equal than amount
    return balance.compareTo(amount) >= 0;
  }

  public UserBalance writeNewUserBalance(User user, BigDecimal newBalance) {
    UserBalance userBalance = userBalanceRepository.getByUserEmailOrThrow(user.getEmail());

    userBalance.setBalance(newBalance);
    return userBalanceRepository.save(userBalance);
  }
}
