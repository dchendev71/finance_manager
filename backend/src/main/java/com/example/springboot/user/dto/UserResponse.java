package com.example.springboot.user.dto;

import com.example.springboot.currency.dto.CurrencyDto;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record UserResponse(
    Long id,
    String email,
    CurrencyDto currency,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
