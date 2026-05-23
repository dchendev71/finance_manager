package com.example.springboot.portfolio;

import com.example.springboot.common.exception.NotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
  // Find by User Id and Portfolio Name
  Optional<Portfolio> findByUserIdAndName(Long userId, String name);

  default Portfolio getByUserIdAndNameOrThrow(Long userId, String name) {
    return this.findByUserIdAndName(userId, name)
        .orElseThrow(() -> new NotFoundException(Portfolio.class, name));
  }
}
