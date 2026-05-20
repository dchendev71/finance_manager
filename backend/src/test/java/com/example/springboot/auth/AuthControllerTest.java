package com.example.springboot.auth;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springboot.auth.dto.AuthRequest;
import com.example.springboot.auth.dto.AuthResponse;
import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.common.exception.ExistsException;
import com.example.springboot.common.exception.InvalidCredentialsException;
import com.example.springboot.common.exception.NotFoundException;
import com.example.springboot.currency.Currency;
import com.example.springboot.helper.AuthTestFactory;
import com.example.springboot.helper.RequestHandler;
import com.example.springboot.helper.UserTestFactory;
import com.example.springboot.security.CustomUserDetailsService;
import com.example.springboot.security.JwtAccessDeniedHandler;
import com.example.springboot.security.JwtAuthenticationEntryPoint;
import com.example.springboot.security.JwtService;
import com.example.springboot.security.SecurityConfig;
import com.example.springboot.user.User;
import com.example.springboot.user.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private AuthService authService;
  @MockitoBean private JwtService jwtService;
  @MockitoBean private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  @MockitoBean private JwtAccessDeniedHandler jwtAccessDeniedHandler;
  @MockitoBean private CustomUserDetailsService customUserDetailsService;

  private RequestHandler requestHandler;
  // Standard /register test variable
  private UserCreateRequest userCreateRequest;
  private UserResponse userResponse;

  // Standard /login test variable
  private AuthRequest authRequest;
  private AuthResponse authResponse;

  @BeforeEach
  void setUp() {
    this.requestHandler = new RequestHandler(mockMvc, objectMapper);
    this.userCreateRequest = UserTestFactory.createUserRequest();
    this.userResponse = UserTestFactory.createUserResponse();

    this.authRequest = AuthTestFactory.createAuthRequest();
    this.authResponse = AuthTestFactory.createAuthResponse();
  }

  @Test
  @DisplayName("POST /register: should return 201 when valid request")
  void register_shouldReturn201_whenValidRequest() throws Exception {
    // Given
    when(authService.register(userCreateRequest)).thenReturn(userResponse);

    requestHandler
        .performPost(ApiRoutes.Auth.REGISTER, userCreateRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(UserTestFactory.testId))
        .andExpect(jsonPath("$.email").value(UserTestFactory.testEmail))
        .andExpect(jsonPath("$.currency.code").value(UserTestFactory.testCurrencyCode));
  }

  @Test
  @DisplayName("POST /register: should return 400 when email is invalid")
  void register_shouldReturn400_whenEmailInvalid() throws Exception {
    // Given
    UserCreateRequest request =
        UserTestFactory.createUserRequest("not-an-email", "password123", "USD");
    requestHandler
        .performPost(ApiRoutes.Auth.REGISTER, request)
        .andExpect(status().isBadRequest())
        .andExpect(
            response ->
                assertTrue(
                    response.getResolvedException() instanceof MethodArgumentNotValidException));
  }

  @Test
  @DisplayName("POST /register: should return 400 when password too short")
  void register_shouldReturn400_whenPasswordTooShort() throws Exception {
    // Given
    UserCreateRequest request =
        UserTestFactory.createUserRequest("john@example.com", "short", "USD");
    // When / Then
    requestHandler
        .performPost(ApiRoutes.Auth.REGISTER, request)
        .andExpect(status().isBadRequest())
        .andExpect(
            response ->
                assertTrue(
                    response.getResolvedException() instanceof MethodArgumentNotValidException));
  }

  @Test
  @DisplayName("POST /register: should return 400 when currency code invalid (3 upper case)")
  void register_shouldReturn400_whenCurrencyCodeInvalid() throws Exception {
    // Given — lowercase currency code
    UserCreateRequest request =
        UserTestFactory.createUserRequest("john@example.com", "password123", "usd");
    requestHandler
        .performPost(ApiRoutes.Auth.REGISTER, request)
        .andExpect(status().isBadRequest())
        .andExpect(
            response ->
                assertTrue(
                    response.getResolvedException() instanceof MethodArgumentNotValidException));
  }

  @Test
  @DisplayName("POST /register: should return 400 when currency code doesn't exist")
  void register_shouldReturn400_whenCurrencyNotFound() throws Exception {
    UserCreateRequest request =
        UserTestFactory.createUserRequest("john@example.com", "password123", "XYZ");
    when(authService.register(request))
        .thenThrow(new NotFoundException(Currency.class, request.currencyCode()));

    requestHandler
        .performPost(ApiRoutes.Auth.REGISTER, request)
        .andExpect(status().isBadRequest())
        .andExpect(
            response -> assertTrue(response.getResolvedException() instanceof NotFoundException));
  }

  @Test
  @DisplayName("POST /register: should return 409 when email already used")
  void register_shouldReturn409_whenEmailAlreadyExists() throws Exception {
    when(authService.register(userCreateRequest))
        .thenThrow(new ExistsException(User.class, userCreateRequest.email()));
    requestHandler
        .performPost(ApiRoutes.Auth.REGISTER, userCreateRequest)
        .andExpect(status().isConflict())
        .andExpect(resp -> assertTrue(resp.getResolvedException() instanceof ExistsException));
  }

  // Login

  @Test
  @DisplayName("POST /login: should return 200 with a JwtToken")
  void login_shouldReturn200() throws Exception {
    when(authService.login(authRequest)).thenReturn(authResponse);
    requestHandler
        .performPost(ApiRoutes.Auth.LOGIN, authRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.jwtToken").value(AuthTestFactory.testJwt));
  }

  @Test
  @DisplayName("POST /login: should return 401 InvalidCredentialsException")
  void login_shouldReturn401() throws Exception {

    when(authService.login(authRequest)).thenThrow(new InvalidCredentialsException());

    requestHandler
        .performPost(ApiRoutes.Auth.LOGIN, authRequest)
        .andExpect(status().is4xxClientError())
        .andExpect(
            response ->
                assertTrue(response.getResolvedException() instanceof InvalidCredentialsException));
  }
}
