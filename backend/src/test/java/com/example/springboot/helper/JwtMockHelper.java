package com.example.springboot.helper;

import static org.mockito.Mockito.when;

import com.example.springboot.security.CustomUserDetailsService;
import com.example.springboot.security.CustomUserPrincipal;
import com.example.springboot.security.JwtService;
import com.example.springboot.user.User;

public class JwtMockHelper {
  public static String mockToken = "mockToken";

  // Make sure that JWT validate the current email
  private static void mockJwt(String email, JwtService jwtService) {
    when(jwtService.extractEmail(mockToken)).thenReturn(email);
    when(jwtService.isTokenValid(mockToken, email)).thenReturn(true);
  }

  private static void mockAuthorities(
      User user, CustomUserDetailsService customUserDetailsService) {
    CustomUserPrincipal userPrincipal = new CustomUserPrincipal(user);

    when(customUserDetailsService.loadUserByUsername(user.getEmail())).thenReturn(userPrincipal);
  }

  public static void mockAuthorization(
      User user, JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
    // JWT
    mockJwt(user.getEmail(), jwtService);

    // Authorities
    mockAuthorities(user, customUserDetailsService);
  }
}
