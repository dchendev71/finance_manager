package com.example.springboot.balance.dto;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record UserBalanceRequest(
    @Positive(message = "Increase amount should be positive") BigDecimal increaseAmount) {}
