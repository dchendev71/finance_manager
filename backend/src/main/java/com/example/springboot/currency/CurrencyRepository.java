package com.example.springboot.repositories;

import com.example.springboot.entities.Currency;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
  Optional<Currency> findByCode(String code);
}
