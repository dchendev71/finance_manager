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
}
