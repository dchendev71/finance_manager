package com.example.springboot.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springboot.auth.dto.AuthRequest;
import com.example.springboot.auth.dto.AuthResponse;
import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.common.exception.InvalidCredentialsException;
import com.example.springboot.helper.AuthTestFactory;
import com.example.springboot.helper.UserTestFactory;
import com.example.springboot.security.JwtService;
import com.example.springboot.user.UserService;
import com.example.springboot.user.dto.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock private UserService userService;
  @Mock private JwtService jwtService;
  @Mock private AuthenticationManager authenticationManager;

  @InjectMocks private AuthService authService;

  @Nested
  @DisplayName("register Tests")
  class RegisterTests {

    @Test
    @DisplayName("should delegate registration cleanly to UserService")
    void register_shouldCallUserServiceAndReturnResponse() {
      // Given
      UserCreateRequest request = UserTestFactory.createUserRequest();
      UserResponse expectedResponse = UserTestFactory.createUserResponse();

      when(userService.registerNewUser(request)).thenReturn(expectedResponse);

      // When
      UserResponse actualResponse = authService.register(request);

      // Then
      assertThat(actualResponse).isEqualTo(expectedResponse);
      verify(userService).registerNewUser(request);
    }
  }

  @Nested
  @DisplayName("login Tests")
  class LoginTests {

    @Test
    @DisplayName("should return AuthResponse when credentials are correct")
    void login_shouldReturnToken_whenCredentialsAreValid() {
      // Given
      AuthRequest request = AuthTestFactory.createAuthRequest();
      String mockJwt = "mocked-jwt-token-string";

      // We stub jwtService because we need its output payload for the response assertion
      when(jwtService.generateToken(request.email())).thenReturn(mockJwt);
      // When
      AuthResponse response = authService.login(request);
      // Then
      assertThat(response).isNotNull();
      assertThat(response.jwtToken()).isEqualTo(mockJwt);

      // CRITICAL: Verify Spring Security was actually engaged with correct values
      verify(authenticationManager)
          .authenticate(
              new UsernamePasswordAuthenticationToken(request.email(), request.password()));
      verify(jwtService).generateToken(request.email());
    }

    @Test
    @DisplayName("should bubble up exception and skip token generation when authentication fails")
    void login_shouldThrowException_whenCredentialsAreInvalid() {
      // Given
      AuthRequest request = new AuthRequest(UserTestFactory.testEmail, "wrong-password");

      // Force the authentication manager to fail out
      when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
          .thenThrow(new InvalidCredentialsException());
      // When / Then
      assertThatThrownBy(() -> authService.login(request))
          .isInstanceOf(InvalidCredentialsException.class);
      // SECURITY VERIFICATION: Ensure token generation is NEVER executed if auth fails
      verify(jwtService, never()).generateToken(anyString());
    }
  }
}
