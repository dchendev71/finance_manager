package com.example.springboot.user;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springboot.common.exception.CurrencyNotFoundException;
import com.example.springboot.common.exception.EmailAlreadyExistsException;
import com.example.springboot.config.SecurityConfig;
import com.example.springboot.helper.UserTestFactory;
import com.example.springboot.user.dto.UserCreateRequest;
import com.example.springboot.user.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private UserService userService;

  private String authRegisterRoute = "/api/v1/auth/register";

  // ─── POST /api/v1/auth/register ─────────────────────────────────────────

  @Test
  @DisplayName("POST /register: should return 201 when valid request")
  void register_shouldReturn201_whenValidRequest() throws Exception {
    // Given
    UserCreateRequest request = UserTestFactory.createUserRequest();
    UserResponse response = UserTestFactory.createUserResponse();
    when(userService.registerNewUser(request)).thenReturn(response);
    // When / Then
    mockMvc
        .perform(
            post(authRegisterRoute)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
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
    // When / Then
    mockMvc
        .perform(
            post(authRegisterRoute)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
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
    mockMvc
        .perform(
            post(authRegisterRoute)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
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
    mockMvc
        .perform(
            post(authRegisterRoute)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
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
    when(userService.registerNewUser(request))
        .thenThrow(new CurrencyNotFoundException(request.currencyCode()));
    mockMvc
        .perform(
            post(authRegisterRoute)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(
            response ->
                assertTrue(response.getResolvedException() instanceof CurrencyNotFoundException));
  }

  @Test
  @DisplayName("POST /register: should return 409 when email already used")
  void register_shouldReturn409_whenEmailAlreadyExists() throws Exception {
    UserCreateRequest request = UserTestFactory.createUserRequest();
    UserResponse response = UserTestFactory.createUserResponse();
    when(userService.registerNewUser(request))
        .thenReturn(response)
        .thenThrow(new EmailAlreadyExistsException(request.email()));
    mockMvc.perform(
        post(authRegisterRoute)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));
    mockMvc
        .perform(
            post(authRegisterRoute)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isConflict())
        .andExpect(
            resp -> assertTrue(resp.getResolvedException() instanceof EmailAlreadyExistsException));
  }
}
