package com.example.springboot.price;

import com.example.springboot.asset.Asset;
import com.example.springboot.asset.AssetService;
import com.example.springboot.common.exception.NotFoundException;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Getter
public class PriceService {

  private static final Duration PRICE_TTL = Duration.ofSeconds(10);
  private static final String PRICE_KEY_PREFIX = "price:";

  private final RedisTemplate<String, Double> redisTemplate;
  private final AssetService assetService;

  // Thread-safe list, updated dynamically
  private final CopyOnWriteArrayList<String> watchedAssets = new CopyOnWriteArrayList<>();

  public PriceService(RedisTemplate<String, Double> redisTemplate, AssetService assetService) {
    this.redisTemplate = redisTemplate;
    this.assetService = assetService;
  }

  @PostConstruct
  public void init() {
    refreshWatchedAssets();
  }

  @Scheduled(fixedRate = 60_000)
  public void refreshWatchedAssets() {
    List<Asset> assets = assetService.getAssets();
    List<String> symbols = assets.stream().map((asset) -> (asset.getTickerSymbol())).toList();
    watchedAssets.clear();
    watchedAssets.addAll(symbols);
  }

  public double getIndicativePrice(String symbol) {
    Double price = redisTemplate.opsForValue().get(PRICE_KEY_PREFIX + symbol);
    if (price == null)
      throw new NotFoundException(PriceService.class, "No cached price for " + symbol);
    return price;
  }
}
