package com.example.springboot.unit.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springboot.asset.Asset;
import com.example.springboot.asset.dto.AssetResponse;
import com.example.springboot.asset.mapper.AssetMapper;
import com.example.springboot.config.TestConfig;
import com.example.springboot.helper.EntityTestFactory;
import com.example.springboot.helper.ResponseTestFactory;
import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.dto.PortfolioResponse;
import com.example.springboot.portfolio.mapper.PortfolioMapper;
import com.example.springboot.portfolio_asset.PortfolioAsset;
import com.example.springboot.portfolio_asset.dto.PortfolioAssetResponse;
import com.example.springboot.portfolio_asset.mapper.PortfolioAssetMapperImpl;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PortfolioAssetMapperTest {

  // 1. Mock the nested mappers declared in your 'uses' attribute
  @Mock private AssetMapper assetMapper;

  @Mock private PortfolioMapper portfolioMapper;

  // 2. InjectMocks instantiates the real PortfolioAssetMapperImpl class
  // and injects the two mock mappers into its constructor
  @InjectMocks private PortfolioAssetMapperImpl portfolioAssetMapper;

  @Test
  void shouldMapToEntityCorrectly() {
    // Arrange
    Portfolio portfolio = EntityTestFactory.PortfolioFactory.create();
    Asset asset = EntityTestFactory.AssetFactory.create();
    BigDecimal quantity = TestConfig.PortfolioAsset.quantity;

    PortfolioAsset entity = portfolioAssetMapper.toEntity(portfolio, asset, quantity);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getPortfolio()).isEqualTo(portfolio);
    assertThat(entity.getAsset()).isEqualTo(asset);
    assertThat(entity.getQuantity()).isEqualByComparingTo(TestConfig.PortfolioAsset.quantity);
  }

  @Test
  void shouldMapToResponseCorrectly() {
    Portfolio portfolio = EntityTestFactory.PortfolioFactory.create();
    Asset asset = EntityTestFactory.AssetFactory.create();
    PortfolioAsset portfolioAsset = EntityTestFactory.PortfolioAssetFactory.create();

    PortfolioResponse expectedPortfolioResponse = ResponseTestFactory.Portfolio.create();
    AssetResponse expectedAssetResponse = ResponseTestFactory.Asset.create();

    // Stub the nested mappers so they don't return null
    Mockito.when(portfolioMapper.toResponse(portfolio)).thenReturn(expectedPortfolioResponse);
    Mockito.when(assetMapper.toResponse(asset)).thenReturn(expectedAssetResponse);

    // Act
    PortfolioAssetResponse response =
        portfolioAssetMapper.toResponse(portfolioAsset, TestConfig.PortfolioAsset.meanPrice);

    // Assert
    assertThat(response).isNotNull();
    assertThat(response.portfolioResponse()).isEqualTo(expectedPortfolioResponse);
    assertThat(response.assetResponse()).isEqualTo(expectedAssetResponse);
  }
}
