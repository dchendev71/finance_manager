package com.example.springboot.portfolio.asset.dto;

import com.example.springboot.portfolio.asset_type.dto.AssetTypeResponse;

public record AssetResponse(
    String name, String tickerSymbol, AssetTypeResponse assetTypeResponse) {}
