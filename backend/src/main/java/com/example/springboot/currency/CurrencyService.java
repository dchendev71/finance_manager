package com.example.springboot.currency;

import com.example.springboot.common.exception.NotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {
  private final CurrencyRepository currencyRepository;

  @Autowired
  public CurrencyService(CurrencyRepository currencyRepository) {
    this.currencyRepository = currencyRepository;
  }

  @Cacheable(value = "currencies", key = "list")
  public List<Currency> getCurrencies() {
    return currencyRepository.findAll();
  }

  public Currency findByCode(String code) {
    return currencyRepository
        .findByCode(code)
        .orElseThrow(() -> new NotFoundException(Currency.class, code));
  }
}
