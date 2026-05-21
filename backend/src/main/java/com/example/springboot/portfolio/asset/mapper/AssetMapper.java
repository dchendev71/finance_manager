package com.example.springboot.portfolio.asset.mapper;

import com.example.springboot.portfolio.asset.Asset;
import com.example.springboot.portfolio.asset.dto.AssetResponse;
import com.example.springboot.portfolio.asset_type.AssetType;
import com.example.springboot.portfolio.asset_type.mapper.AssetTypeMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {AssetTypeMapper.class},
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AssetMapper {
  @Mapping(target = "id", ignore = true)
  Asset toEntity(String name, String tickerSymbol, AssetType assetType);

  @Mapping(target = "assetTypeResponse", source = "asset.assetType")
  AssetResponse toResponse(Asset asset);
}
