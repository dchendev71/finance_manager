package com.example.springboot.price;

import com.example.springboot.asset.Asset;
import com.example.springboot.asset.AssetService;
import com.example.springboot.common.exception.NotFoundException;
import com.example.springboot.common.exception.RateLimitException;
import com.example.springboot.web_client.MarketApiClient;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PriceService {

  private static final Duration PRICE_TTL = Duration.ofSeconds(10);
  private static final String PRICE_KEY_PREFIX = "price:";

  private final RedisTemplate<String, Double> redisTemplate;
  private final MarketApiClient marketApiClient;
  private final AssetService assetService;

  // Thread-safe list, updated dynamically
  private final CopyOnWriteArrayList<String> watchedAssets = new CopyOnWriteArrayList<>();

  public PriceService(
      RedisTemplate<String, Double> redisTemplate,
      MarketApiClient marketApiClient,
      AssetService assetService) {
    this.redisTemplate = redisTemplate;
    this.marketApiClient = marketApiClient;
    this.assetService = assetService;
  }

  @PostConstruct
  public void init() {
    refreshWatchedAssets();
    refreshPrices();
  }

  @Scheduled(fixedRate = 60_000)
  public void refreshWatchedAssets() {
    List<Asset> assets = assetService.getAssets();
    List<String> symbols = assets.stream().map((asset) -> (asset.getTickerSymbol())).toList();
    watchedAssets.clear();
    watchedAssets.addAll(symbols);
  }

  @Scheduled(fixedRate = 10_000)
  public void refreshPrices() {
    watchedAssets.forEach(
        symbol -> {
          try {
            double price = marketApiClient.fetchPrice(symbol);
            redisTemplate.opsForValue().set(PRICE_KEY_PREFIX + symbol, price, PRICE_TTL);
          } catch (RateLimitException ex) {
            System.out.println("RATE LIMIT: " + ex.getMessage() + " — skipping this cycle");
          } catch (NotFoundException ex) {
            System.out.println("WARNING: " + ex.getMessage());
          } catch (Exception ex) {
            System.out.println("UNEXPECTED ERROR for " + symbol + ": " + ex.getMessage());
          }
        });
  }

  public double getIndicativePrice(String symbol) {
    Double price = redisTemplate.opsForValue().get(PRICE_KEY_PREFIX + symbol);
    if (price == null)
      throw new NotFoundException(PriceService.class, "No cached price for " + symbol);
    return price;
  }

  public double getRealTimePrice(String symbol) {
    return marketApiClient.fetchPrice(symbol);
  }
}
