package com.example.springboot.portfolio.asset;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {
  Optional<String> findByTickerSymbol(String tickerSymbol);
}
