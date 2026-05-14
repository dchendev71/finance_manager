package com.example.springboot.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthRequest(@NotBlank @Email String email, @NotBlank String password) {}
