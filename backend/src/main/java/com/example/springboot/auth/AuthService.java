package com.example.springboot.auth;

import com.example.springboot.auth.dto.AuthRequest;
import com.example.springboot.auth.dto.AuthResponse;
import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.security.CustomUserPrincipal;
import com.example.springboot.security.JwtService;
import com.example.springboot.user.UserService;
import com.example.springboot.user.dto.UserResponse;
import com.example.springboot.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserMapper userMapper;
  private final UserService userService;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public UserResponse register(UserCreateRequest request) {
    return userService.registerNewUser(request);
  }

  public AuthResponse login(AuthRequest request) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password()));

    CustomUserPrincipal userDetails = (CustomUserPrincipal) authentication.getPrincipal();
    // Generate JWT token
    String jwtToken = jwtService.generateToken(userDetails.getUsername());
    UserResponse userResponse = userMapper.toResponse(userDetails.getUser());

    return new AuthResponse(jwtToken, userResponse);
  }
}
