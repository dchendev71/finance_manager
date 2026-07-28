package com.example.springboot.asset;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssetService {
  private final AssetRepository assetRepository;

  @Cacheable(value = "assets")
  public List<Asset> getAssets() {
    return assetRepository.findAll();
  }

  // This function is a helper function for the WebSocketInitializer and redis cache hit
  public String translatedSymbol(Asset asset) {
    if (asset.getAssetType().getType().equals("cryptocurrency")) {
      return "BINANCE:" + asset.getTickerSymbol() + "USDT";
    }
    return asset.getTickerSymbol();
  }
}
