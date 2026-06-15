package com.example.springboot.currency;

import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.currency.dto.CurrencyDto;
import com.example.springboot.currency.mapper.CurrencyMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CurrencyController {
  private final CurrencyService currencyService;
  private final CurrencyMapper currencyMapper;

  @GetMapping(ApiRoutes.Currency.BASE)
  public ResponseEntity<List<CurrencyDto>> getCurrencies() {
    List<Currency> currencies = currencyService.getCurrencies();
    List<CurrencyDto> currencyDtos = currencies.stream().map(currencyMapper::toDto).toList();

    return ResponseEntity.ok(currencyDtos);
  }
}
