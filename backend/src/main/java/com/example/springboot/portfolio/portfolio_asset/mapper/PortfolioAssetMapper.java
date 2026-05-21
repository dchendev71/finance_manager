package com.example.springboot.portfolio.portfolio_asset.mapper;

import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.asset.Asset;
import com.example.springboot.portfolio.asset.mapper.AssetMapper;
import com.example.springboot.portfolio.mapper.PortfolioMapper;
import com.example.springboot.portfolio.portfolio_asset.PortfolioAsset;
import java.math.BigDecimal;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {AssetMapper.class, PortfolioMapper.class},
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PortfolioAssetMapper {
  @Mapping(target = "id", ignore = true)
  PortfolioAsset toEntity(Portfolio portfolio, Asset asset, BigDecimal quantity);
}
