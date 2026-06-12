package com.example.springboot.portfolio;

import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.portfolio.dto.PortfolioCreateRequest;
import com.example.springboot.portfolio.dto.PortfolioResponse;
import com.example.springboot.portfolio.mapper.PortfolioMapper;
import com.example.springboot.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PortfolioController {
  private final PortfolioService portfolioService;
  private final PortfolioMapper portfolioMapper;

  @PostMapping(path = ApiRoutes.Portfolios.CREATE)
  public ResponseEntity<PortfolioResponse> createPortfolio(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @Valid @RequestBody PortfolioCreateRequest request) {
    PortfolioResponse response =
        portfolioMapper.toResponse(
            portfolioService.createPortfolio(principal.getUsername(), request));
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // TODO: Test function
  @GetMapping(path = ApiRoutes.Portfolios.LIST)
  public ResponseEntity<List<PortfolioResponse>> getPortfolios(
      @AuthenticationPrincipal CustomUserPrincipal principal) {
    List<Portfolio> portfolios = portfolioService.getPortfolios(principal.getUsername());
    List<PortfolioResponse> portfolioResponses =
        portfolios.stream().map(portfolioMapper::toResponse).toList();

    return ResponseEntity.ok(portfolioResponses);
  }
}
