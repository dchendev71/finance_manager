package com.example.springboot.unit.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springboot.config.TestConfig;
import com.example.springboot.currency.Currency;
import com.example.springboot.currency.dto.CurrencyDto;
import com.example.springboot.currency.mapper.CurrencyMapper;
import com.example.springboot.currency.mapper.CurrencyMapperImpl;
import com.example.springboot.helper.EntityTestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(CurrencyMapperImpl.class)
class CurrencyMapperTest {

  @Autowired private CurrencyMapper currencyMapper;

  @Test
  @DisplayName("toDto: should map Currency to CurrencyDto")
  void toDto_shouldMapAllFields() {
    // Given
    Currency currency = EntityTestFactory.CurrencyFactory.create();
    // When
    CurrencyDto dto = currencyMapper.toDto(currency);
    // Then
    assertThat(dto).isNotNull();
    assertThat(dto.code()).isEqualTo(TestConfig.Currency.code);
    assertThat(dto.name()).isEqualTo(TestConfig.Currency.name);
    assertThat(dto.symbol()).isEqualTo(TestConfig.Currency.symbol);
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
    CurrencyDto dto = EntityTestFactory.CurrencyFactory.createDto();

    Currency currency = currencyMapper.toEntity(dto);

    assertThat(currency).isNotNull();
    assertThat(currency.getCode()).isEqualTo(TestConfig.Currency.code);
    assertThat(currency.getName()).isEqualTo(TestConfig.Currency.name);
    assertThat(currency.getSymbol()).isEqualTo(TestConfig.Currency.symbol);
  }

  @Test
  @DisplayName("toEntity: should ignore audit fields")
  void toEntity_shouldIgnoreAuditFields() {
    CurrencyDto dto = EntityTestFactory.CurrencyFactory.createDto();
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
