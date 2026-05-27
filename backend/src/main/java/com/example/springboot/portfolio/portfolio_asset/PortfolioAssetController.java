package com.example.springboot.portfolio.portfolio_asset;

import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.portfolio.portfolio_asset.dto.PortfolioAssetRequest;
import com.example.springboot.portfolio.portfolio_asset.dto.PortfolioAssetResponse;
import com.example.springboot.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  // TODO: write test
  @GetMapping(path = ApiRoutes.Portfolio.PortfolioAsset.LIST)
  public ResponseEntity<List<PortfolioAssetResponse>> getPortfolioAssets(
      @AuthenticationPrincipal CustomUserPrincipal principal, @PathVariable String portfolioName) {
    return ResponseEntity.ok(
        portfolioAssetService.getPortfolioAssets(principal.getUsername(), portfolioName));
  }

  @PostMapping(path = ApiRoutes.Portfolio.PortfolioAsset.CREATE)
  public ResponseEntity<PortfolioAssetResponse> createPortfolioAsset(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @Valid @RequestBody PortfolioAssetRequest request) {

    PortfolioAssetResponse response =
        portfolioAssetService.createPortfolioAsset(principal.getUsername(), request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // TODO: write test
  @DeleteMapping(path = ApiRoutes.Portfolio.PortfolioAsset.DELETE)
  public ResponseEntity<Void> deletePortfolioAsset(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @PathVariable String portfolioName,
      @PathVariable String assetName) {

    portfolioAssetService.deletePortfolioAsset(principal.getUsername(), portfolioName, assetName);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping(path = ApiRoutes.Portfolio.PortfolioAsset.UPDATE)
  public ResponseEntity<PortfolioAssetResponse> updatePortfolioAsset(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @Valid @RequestBody PortfolioAssetRequest request) {
    Optional<PortfolioAssetResponse> response =
        portfolioAssetService.updatePortfolioAsset(principal.getUsername(), request);
    if (response.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(response.get());
  }
}
