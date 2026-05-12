package com.example.springboot.currency.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springboot.currency.Currency;
import com.example.springboot.currency.dto.CurrencyDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class CurrencyMapperTest {
  private CurrencyMapper currencyMapper;

  @BeforeEach
  void setUp() {
    currencyMapper = Mappers.getMapper(CurrencyMapper.class);
  }

  @Test
  @DisplayName("toDto: should map Currency to CurrencyDto")
  void toDto_shouldMapAllFields() {
    // Given
    Currency currency = Currency.builder().id(1L).code("USD").name("US Dollar").symbol("$").build();
    // When
    CurrencyDto dto = currencyMapper.toDto(currency);
    // Then
    assertThat(dto).isNotNull();
    assertThat(dto.id()).isEqualTo(1L);
    assertThat(dto.code()).isEqualTo("USD");
    assertThat(dto.name()).isEqualTo("US Dollar");
    assertThat(dto.symbol()).isEqualTo("$");
  }

  @Test
  @DisplayName("toDto: should return null when Currency is null")
  void toDto_shoudReturnNull_whenCurrencyIsNull() {
    CurrencyDto dto = currencyMapper.toDto(null);
    assertThat(dto).isNull();
  }

  @Test
  @DisplayName("toEntity: should map CurrencyDto to Currency")
  void toEntity_shouldMapAllFields() {
    CurrencyDto dto =
        CurrencyDto.builder().id(1L).code("USD").name("US Dollar").symbol("$").build();

    Currency currency = currencyMapper.toEntity(dto);

    assertThat(currency).isNotNull();
    assertThat(currency.getId()).isEqualTo(1L);
    assertThat(currency.getCode()).isEqualTo("USD");
    assertThat(currency.getName()).isEqualTo("US Dollar");
    assertThat(currency.getSymbol()).isEqualTo("$");
  }

  @Test
  @DisplayName("toEntity: should ignore audit fields")
  void toEntity_shouldIgnoreAuditFields() {

    CurrencyDto dto =
        CurrencyDto.builder().id(1L).code("USD").name("US Dollar").symbol("$").build();
    Currency currency = currencyMapper.toEntity(dto);

    assertThat(currency).isNotNull();
    assertThat(currency.getCreatedAt()).isNull();
    assertThat(currency.getUpdatedAt()).isNull();
  }

  @Test
  @DisplayName("toEntity: should return null when CurrencyDto is null")
  void toEntity_shouldReturnNull_whenDtoIsNull() {
    Currency currency = currencyMapper.toEntity(null);
    assertThat(currency).isNull();
  }
}
