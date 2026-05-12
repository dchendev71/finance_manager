package com.example.springboot.currency.mapper;

import com.example.springboot.currency.Currency;
import com.example.springboot.currency.dto.CurrencyDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
  CurrencyDto toDto(Currency currency);

  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @BeanMapping(builder = @org.mapstruct.Builder(disableBuilder = false))
  Currency toEntity(CurrencyDto currencyDto);
}
