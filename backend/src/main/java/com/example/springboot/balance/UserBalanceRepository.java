package com.example.springboot.balance;

import com.example.springboot.common.exception.NotFoundException;
import com.example.springboot.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {
  Optional<UserBalance> findByUserEmail(String email);

  default UserBalance getByUserEmailOrThrow(String email) {
    return this.findByUserEmail(email).orElseThrow(() -> new NotFoundException(User.class, email));
  }
}
