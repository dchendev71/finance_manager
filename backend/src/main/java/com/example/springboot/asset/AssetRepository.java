package com.example.springboot.asset;

import com.example.springboot.common.exception.NotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {
  // Name value is unique
  Optional<Asset> findByName(String name);

  default Asset getByNameOrThrow(String name) {
    return this.findByName(name).orElseThrow(() -> new NotFoundException(Asset.class, name));
  }
}
