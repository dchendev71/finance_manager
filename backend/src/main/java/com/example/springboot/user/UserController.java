package com.example.springboot.user;

import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.security.CustomUserPrincipal;
import com.example.springboot.user.dto.ChangeCurrencyRequest;
import com.example.springboot.user.dto.ChangeEmailRequest;
import com.example.springboot.user.dto.ChangePasswordRequest;
import com.example.springboot.user.dto.UserResponse;
import com.example.springboot.user.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final UserMapper userMapper;

  @PostMapping(ApiRoutes.Users.CHANGE_PASSWORD)
  public ResponseEntity<UserResponse> changePassword(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @Valid @RequestBody ChangePasswordRequest request) {
    UserResponse response =
        userMapper.toResponse(userService.changePassword(principal.getUsername(), request));
    return ResponseEntity.ok(response);
  }

  @PatchMapping(ApiRoutes.Users.CHANGE_EMAIL)
  public ResponseEntity<UserResponse> changeEmail(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @Valid @RequestBody ChangeEmailRequest changeEmailRequest) {
    UserResponse response =
        userMapper.toResponse(userService.changeEmail(principal.getUsername(), changeEmailRequest));

    return ResponseEntity.ok(response);
  }

  @PatchMapping(ApiRoutes.Users.CHANGE_CURRENCY)
  public ResponseEntity<UserResponse> changeCurrency(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @Valid @RequestBody ChangeCurrencyRequest changeCurrencyRequest) {
    UserResponse response =
        userMapper.toResponse(
            userService.changeCurrency(principal.getUsername(), changeCurrencyRequest));

    return ResponseEntity.ok(response);
  }

  @DeleteMapping(ApiRoutes.Users.DELETE)
  public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal CustomUserPrincipal principal) {
    userService.deleteUser(principal.getUsername());
    return ResponseEntity.noContent().build();
  }
}
