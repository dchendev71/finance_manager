package com.example.springboot.helper;

import com.example.springboot.auth.dto.AuthRequest;
import com.example.springboot.auth.dto.AuthResponse;

public class AuthTestFactory {
  public static String testEmail = UserTestFactory.testEmail;
  public static String testPassword = UserTestFactory.testPassword;

  public static String testJwt =
      "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqb2huMTI0QGdtYWlsLmNvbSIsImlhdCI6MTc3OTAxNjI5OCwiZXhwIjoxNzc5MDE5ODk4fQ.4qjsfdmWnNeiFRNISkVsl75SvTDAxrCEdApgt_IW9JMiM2bo02knRTpuODAq0f6L";

  public static AuthRequest createAuthRequest() {
    return AuthRequest.builder().email(testEmail).password(testPassword).build();
  }

  public static AuthResponse createAuthResponse() {
    return new AuthResponse(testJwt);
  }
}
