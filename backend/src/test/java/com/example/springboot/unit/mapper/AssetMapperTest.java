package com.example.springboot.unit.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springboot.asset.Asset;
import com.example.springboot.asset.dto.AssetResponse;
import com.example.springboot.asset.mapper.AssetMapperImpl;
import com.example.springboot.asset_type.AssetType;
import com.example.springboot.asset_type.dto.AssetTypeResponse;
import com.example.springboot.asset_type.mapper.AssetTypeMapper;
import com.example.springboot.config.TestConfig;
import com.example.springboot.helper.EntityTestFactory;
import com.example.springboot.helper.ResponseTestFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssetMapperTest {

  // 1. Mock the nested mapper declared in the 'uses' attribute
  @Mock private AssetTypeMapper assetTypeMapper;

  // 2. InjectMocks instantiates AssetMapperImpl and passes the mocked assetTypeMapper into its
  // constructor
  @InjectMocks private AssetMapperImpl assetMapper;

  @Test
  void shouldMapToEntityCorrectly() {
    // Act
    AssetType assetType = EntityTestFactory.AssetTypeFactory.create();
    Asset entity =
        assetMapper.toEntity(TestConfig.Asset.name, TestConfig.Asset.tickerSymbol, assetType);

    // Assert
    assertThat(entity).isNotNull();
    assertThat(entity.getId()).isNull(); // Verifies 'id' is ignored as configured
    assertThat(entity.getName()).isEqualTo(TestConfig.Asset.name);
    assertThat(entity.getTickerSymbol()).isEqualTo(TestConfig.Asset.tickerSymbol);
    assertThat(entity.getAssetType()).isEqualTo(assetType);
  }

  @Test
  void shouldMapToResponseCorrectly() {

    Asset asset = EntityTestFactory.AssetFactory.create();
    AssetType assetType = EntityTestFactory.AssetTypeFactory.create();
    AssetTypeResponse expectedTypeResponse = ResponseTestFactory.AssetType.create();
    // Stub the nested mapper so it doesn't return null
    Mockito.when(assetTypeMapper.toResponse(assetType)).thenReturn(expectedTypeResponse);

    // Act
    AssetResponse response = assetMapper.toResponse(asset);

    // Assert
    assertThat(response).isNotNull();
    assertThat(response.name()).isEqualTo(TestConfig.Asset.name);

    assertThat(response.tickerSymbol()).isEqualTo(TestConfig.Asset.tickerSymbol);
    // Verifies the target name 'assetTypeResponse' gets the stubbed response
    assertThat(response.assetTypeResponse()).isEqualTo(expectedTypeResponse);
  }

  @Test
  void shouldReturnNullWhenSourceIsNull() {
    // MapStruct gracefully handles null roots by default
    assertThat(assetMapper.toResponse(null)).isNull();
  }
}
