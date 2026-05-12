package com.example.springboot.user.dto;

import com.example.springboot.currency.dto.CurrencyDto;
import java.time.LocalDateTime;

public record UserResponse(
    Long id,
    String email,
    CurrencyDto currency,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
