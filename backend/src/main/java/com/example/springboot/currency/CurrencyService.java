package com.example.springboot.currency;

import com.example.springboot.common.exception.CurrencyNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {
  private final CurrencyRepository currencyRepository;

  @Autowired
  public CurrencyService(CurrencyRepository currencyRepository) {
    this.currencyRepository = currencyRepository;
  }

  public Currency findByCode(String code) {
    return currencyRepository
        .findByCode(code)
        .orElseThrow(() -> new CurrencyNotFoundException(code));
  }
}
