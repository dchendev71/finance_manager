package com.example.springboot.asset.dto;

import com.example.springboot.asset_type.dto.AssetTypeResponse;

public record AssetResponse(
    String name, String tickerSymbol, AssetTypeResponse assetTypeResponse) {}
