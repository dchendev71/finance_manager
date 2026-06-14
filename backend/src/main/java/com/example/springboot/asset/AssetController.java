package com.example.springboot.asset;

import com.example.springboot.asset.dto.AssetResponse;
import com.example.springboot.asset.mapper.AssetMapper;
import com.example.springboot.common.config.ApiRoutes;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AssetController {
  private final AssetService assetService;
  private final AssetMapper assetMapper;

  @GetMapping(ApiRoutes.Assets.GET)
  public ResponseEntity<List<AssetResponse>> getAssets() {
    List<Asset> assets = assetService.getAssets();
    List<AssetResponse> assetResponses = assets.stream().map(assetMapper::toResponse).toList();

    return ResponseEntity.ok(assetResponses);
  }
}
