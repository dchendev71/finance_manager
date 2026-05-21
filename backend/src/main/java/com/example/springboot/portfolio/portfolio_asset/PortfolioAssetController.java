package com.example.springboot.portfolio.portfolio_asset;

import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.portfolio.portfolio_asset.dto.CreatePortfolioAssetRequest;
import com.example.springboot.portfolio.portfolio_asset.dto.PortfolioAssetResponse;
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
public class PortfolioAssetController {

  private final PortfolioAssetService portfolioAssetService;

  @Autowired
  public PortfolioAssetController(PortfolioAssetService portfolioAssetService) {
    this.portfolioAssetService = portfolioAssetService;
  }

  @PostMapping(path = ApiRoutes.Portfolio.PortfolioAsset.CREATE_PORTFOLIO_ASSET)
  public ResponseEntity<PortfolioAssetResponse> createPortfolioAsset(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @Valid @RequestBody CreatePortfolioAssetRequest request) {

    PortfolioAssetResponse response =
        portfolioAssetService.createPortfolioAsset(principal.getUsername(), request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
