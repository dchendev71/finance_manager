package com.example.springboot.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangeCurrencyRequest(
    @NotBlank(message = "You have to specify a prefered currency (ISO4217)")
        @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be 3 uppercase letters")
        String currencyCode) {}
