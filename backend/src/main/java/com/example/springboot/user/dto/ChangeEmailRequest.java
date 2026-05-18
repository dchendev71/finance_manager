package com.example.springboot.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ChangeEmailRequest(
    @NotBlank(message = "Please write your current password") String currentPassword,
    @Email(message = "You must specify a valid email")
        @NotBlank(message = "Your new Email can't be empty")
        String newEmail) {}
