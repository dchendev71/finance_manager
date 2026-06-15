package com.example.springboot.asset;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssetService {
  private final AssetRepository assetRepository;

  @Cacheable(value = "assets", key = "list")
  public List<Asset> getAssets() {
    return assetRepository.findAll();
  }
}
