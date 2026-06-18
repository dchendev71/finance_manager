package com.example.springboot.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ChangePasswordRequest(
    @NotBlank(message = "Current password is required") String currentPassword,
    @NotBlank(message = "New password is required")
        @Size(min = 8, message = "New password must be at leat 8 length long")
        String newPassword,
    @NotBlank(message = "Confirm password is required")
        @Size(min = 8, message = "Confirm password must be at least 8 length leng")
        String confirmPassword) {

  // Spring Boot automatically evaluates this during the @Valid check
  @JsonIgnore
  @AssertTrue(message = "New password and confirmation password do not match")
  public boolean isPasswordConfirmed() {
    if (newPassword == null) return false;
    return newPassword.equals(confirmPassword);
  }
}
