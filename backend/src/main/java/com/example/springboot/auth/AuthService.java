package com.example.springboot.auth;

import com.example.springboot.auth.dto.AuthRequest;
import com.example.springboot.auth.dto.AuthResponse;
import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.security.JwtService;
import com.example.springboot.user.UserService;
import com.example.springboot.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserService userService;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public UserResponse register(UserCreateRequest request) {
    return userService.registerNewUser(request);
  }

  public AuthResponse login(AuthRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.password()));

    String jwtToken = jwtService.generateToken(request.email());

    return new AuthResponse(jwtToken);
  }
}
