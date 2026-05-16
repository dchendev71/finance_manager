package com.example.springboot.portfolio;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
  // Find by User Id and Portfolio Name
  Optional<Portfolio> findByUserIdAndName(Long userId, String name);
}
