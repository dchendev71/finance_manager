package com.example.springboot.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserCreateRequest(
    @Email(message = "Email must be valid") @NotBlank(message = "Email is required") String email,
    @NotBlank(message = "Password is required")
        @Size(min = 8, message = "Password must be at least 8 characters")
        String password,
    @NotBlank(message = "You have to specify a prefered currency (ISO4217)")
        @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be 3 uppercase letters")
        String currencyCode) {}
