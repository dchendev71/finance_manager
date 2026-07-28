package com.example.springboot.web_client;

import com.example.springboot.asset.Asset;
import com.example.springboot.asset.AssetService;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@Slf4j
public class WebSocketInitializer {

  private final FinnhubPriceListener listener;
  private final AssetService assetService;

  public WebSocketInitializer(FinnhubPriceListener listener, AssetService assetService) {
    this.listener = listener;
    this.assetService = assetService;
  }

  @PostConstruct
  public void init() {
    List<Asset> assets = assetService.getAssets();
    // For crypto, it is BINANCE:BTCUSDT
    List<String> symbols =
        assets.stream()
            .map(
                (asset) -> {
                  return assetService.translatedSymbol(asset);
                })
            .toList();

    listener.connect(symbols);
  }
}
