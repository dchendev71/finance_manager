package com.example.springboot.portfolio.portfolio_asset.dto;

import com.example.springboot.portfolio.asset.dto.AssetResponse;
import com.example.springboot.portfolio.dto.PortfolioResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;

public record PortfolioAssetResponse(
    PortfolioResponse portfolioResponse,
    AssetResponse assetResponse,
    @JsonFormat(shape = JsonFormat.Shape.STRING) BigDecimal quantity,
    @JsonFormat(shape = JsonFormat.Shape.STRING) BigDecimal meanPrice) {}
