package com.example.springboot.portfolio.portfolio_asset.dto;

import com.example.springboot.portfolio.asset.dto.AssetResponse;
import com.example.springboot.portfolio.dto.PortfolioResponse;
import java.math.BigDecimal;

public record PortfolioAssetResponse(
    PortfolioResponse portfolioResponse,
    AssetResponse assetResponse,
    BigDecimal quantity,
    BigDecimal meanPrice) {}
