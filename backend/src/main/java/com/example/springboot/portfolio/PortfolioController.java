package com.example.springboot.portfolio;

import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.portfolio.dto.PortfolioCreateRequest;
import com.example.springboot.portfolio.dto.PortfolioResponse;
import com.example.springboot.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PortfolioController {
  private final PortfolioService portfolioService;

  @Autowired
  public PortfolioController(PortfolioService portfolioService) {
    this.portfolioService = portfolioService;
  }

  @PostMapping(path = ApiRoutes.Portfolio.CREATE_PORTFOLIO)
  public ResponseEntity<PortfolioResponse> createPortfolio(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @Valid @RequestBody PortfolioCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(portfolioService.createPortfolio(principal.getUsername(), request));
  }
}
