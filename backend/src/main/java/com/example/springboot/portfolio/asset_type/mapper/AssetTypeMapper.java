package com.example.springboot.portfolio.asset_type.mapper;

import com.example.springboot.portfolio.asset_type.AssetType;
import com.example.springboot.portfolio.asset_type.dto.AssetTypeResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AssetTypeMapper {
  AssetType toEntity(String type);

  AssetTypeResponse toResponse(AssetType assetType);
}
