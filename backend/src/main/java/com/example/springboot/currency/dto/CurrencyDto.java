package com.example.springboot.currency.dto;

import lombok.Builder;

@Builder
public record CurrencyDto(Long id, String code, String name, String symbol) {}
