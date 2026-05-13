package com.example.springboot.currency.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springboot.currency.Currency;
import com.example.springboot.currency.dto.CurrencyDto;
import com.example.springboot.helper.CurrencyTestFactory;
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
    Currency currency = CurrencyTestFactory.createCurrency();
    // When
    CurrencyDto dto = currencyMapper.toDto(currency);
    // Then
    assertThat(dto).isNotNull();
    assertThat(dto.id()).isEqualTo(CurrencyTestFactory.testId);
    assertThat(dto.code()).isEqualTo(CurrencyTestFactory.testCode);
    assertThat(dto.name()).isEqualTo(CurrencyTestFactory.testName);
    assertThat(dto.symbol()).isEqualTo(CurrencyTestFactory.testSymbol);
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
    CurrencyDto dto = CurrencyTestFactory.createCurrencyDto();

    Currency currency = currencyMapper.toEntity(dto);

    assertThat(currency).isNotNull();
    // Id is not needed in the transformation
    assertThat(currency.getId()).isEqualTo(null);
    assertThat(currency.getCode()).isEqualTo(CurrencyTestFactory.testCode);
    assertThat(currency.getName()).isEqualTo(CurrencyTestFactory.testName);
    assertThat(currency.getSymbol()).isEqualTo(CurrencyTestFactory.testSymbol);
  }

  @Test
  @DisplayName("toEntity: should ignore audit fields")
  void toEntity_shouldIgnoreAuditFields() {
    CurrencyDto dto = CurrencyTestFactory.createCurrencyDto();
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
