package com.example.springboot.unit.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springboot.config.TestConfig;
import com.example.springboot.helper.EntityTestFactory;
import com.example.springboot.portfolio.asset_type.AssetType;
import com.example.springboot.portfolio.asset_type.dto.AssetTypeResponse;
import com.example.springboot.portfolio.asset_type.mapper.AssetTypeMapper;
import com.example.springboot.portfolio.asset_type.mapper.AssetTypeMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssetTypeMapperTest {

  // Instantiate the real class MapStruct generates at compile time
  private final AssetTypeMapper assetTypeMapper = new AssetTypeMapperImpl();

  @Test
  void shouldMapStringToEntity() {
    // Arrange
    String inputType = TestConfig.AssetType.type;

    // Act
    AssetType result = assetTypeMapper.toEntity(inputType);

    // Assert
    assertThat(result).isNotNull();
    assertThat(result.getType()).isEqualTo(TestConfig.AssetType.type);
  }

  @Test
  void shouldMapEntityToResponse() {
    // Arrange
    AssetType assetType = EntityTestFactory.AssetTypeFactory.create();
    // Act
    AssetTypeResponse response = assetTypeMapper.toResponse(assetType);

    // Assert
    assertThat(response).isNotNull();
    assertThat(response.type()).isEqualTo(TestConfig.AssetType.type);
  }

  @Test
  void shouldReturnNullWhenSourceIsNull() {
    // MapStruct mappers gracefully handle null inputs by default
    assertThat(assetTypeMapper.toEntity(null)).isNull();
    assertThat(assetTypeMapper.toResponse(null)).isNull();
  }
}
