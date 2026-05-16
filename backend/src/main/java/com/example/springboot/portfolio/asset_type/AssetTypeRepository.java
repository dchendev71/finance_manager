package com.example.springboot.portfolio.asset_type;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetTypeRepository extends JpaRepository<AssetType, String> {
  Optional<String> findByType(String time);
}
