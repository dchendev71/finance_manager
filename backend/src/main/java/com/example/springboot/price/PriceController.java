package com.example.springboot.price;

import com.example.springboot.common.config.ApiRoutes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PriceController {
  private final PriceService priceService;

  @GetMapping(ApiRoutes.Price.BASE)
  public ResponseEntity<Double> getPrice(@PathVariable String symbol) {
    return ResponseEntity.ok(priceService.getIndicativePrice(symbol));
  }
}
