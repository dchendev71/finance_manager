package com.example.springboot.asset;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssetService {
  private final AssetRepository assetRepository;

  public List<Asset> getAssets() {
    return assetRepository.findAll();
  }
}
