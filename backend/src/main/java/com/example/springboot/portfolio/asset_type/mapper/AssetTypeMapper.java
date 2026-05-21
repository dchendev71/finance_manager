package com.example.springboot.portfolio.asset_type.mapper;

import com.example.springboot.portfolio.asset_type.AssetType;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AssetTypeMapper {
  AssetType toEntity(String type);
}
