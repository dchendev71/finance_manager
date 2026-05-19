package com.example.springboot.user;

import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.security.CustomUserPrincipal;
import com.example.springboot.user.dto.ChangeEmailRequest;
import com.example.springboot.user.dto.ChangePasswordRequest;
import com.example.springboot.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  public ResponseEntity<UserResponse> registerNewUser(
      @Valid @RequestBody UserCreateRequest request) {
    UserResponse response = userService.registerNewUser(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/change-password")
  public ResponseEntity<UserResponse> changePassword(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @Valid @RequestBody ChangePasswordRequest request) {
    return ResponseEntity.ok(userService.changePassword(principal.getUsername(), request));
  }

  @PutMapping("/change-email")
  public ResponseEntity<UserResponse> changeEmail(
      @AuthenticationPrincipal CustomUserPrincipal principal,
      @Valid @RequestBody ChangeEmailRequest changeEmailRequest) {

    return ResponseEntity.ok(userService.changeEmail(principal.getUsername(), changeEmailRequest));
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal CustomUserPrincipal principal) {
    userService.deleteUser(principal.getUsername());
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/profile")
  public ResponseEntity<UserResponse> getProfile(
      @AuthenticationPrincipal CustomUserPrincipal principal) {
    return ResponseEntity.ok(userService.getProfile(principal.getId()));
  }
}
