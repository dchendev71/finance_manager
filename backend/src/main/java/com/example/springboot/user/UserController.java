package com.example.springboot.user;

import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
}
