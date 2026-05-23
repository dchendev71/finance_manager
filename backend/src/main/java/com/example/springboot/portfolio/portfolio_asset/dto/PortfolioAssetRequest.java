package com.example.springboot.portfolio.portfolio_asset.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record PortfolioAssetRequest(
    @NotBlank(message = "Portfolio name must specify the target portfolio") String portfolioName,
    @NotBlank(message = "Asset name can't be empty") String assetName,
    BigDecimal quantity) {}
