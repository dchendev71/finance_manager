package com.example.springboot.portfolio_asset.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record PortfolioAssetRequest(
    @NotBlank(message = "Asset name can't be empty") String assetName,
    @NotNull(message = "Quantity is required") BigDecimal quantity,
    @NotNull(message = "Unit Price is required")
        @Positive(message = "Unit price must be greater than 0")
        BigDecimal unitPrice) {}
