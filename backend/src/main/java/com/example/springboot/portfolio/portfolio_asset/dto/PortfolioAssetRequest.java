package com.example.springboot.portfolio.portfolio_asset.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record PortfolioAssetRequest(
    @NotBlank(message = "Asset name can't be empty") String assetName,
    BigDecimal quantity,
    BigDecimal unitPrice) {}
