package com.example.springboot.user;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springboot.helper.UserTestFactory;
import com.example.springboot.user.dto.UserCreateRequest;
import com.example.springboot.user.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private UserService userService;

  // ─── POST /api/v1/users/register ─────────────────────────────────────────

  @Test
  @DisplayName("POST /register: should return 201 when valid request")
  void register_shouldReturn201_whenValidRequest() throws Exception {
    // Given
    UserCreateRequest request = UserTestFactory.createUserRequest();
    UserResponse response = UserTestFactory.createUserResponse();
    when(userService.createNewUser(request)).thenReturn(response);
    // When / Then
    mockMvc
        .perform(
            post("/api/v1/users/register")
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
            post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
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
            post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("POST /register: should return 400 when currency code invalid")
  void register_shouldReturn400_whenCurrencyCodeInvalid() throws Exception {
    // Given — lowercase currency code
    UserCreateRequest request =
        UserTestFactory.createUserRequest("john@example.com", "password123", "usd");
    mockMvc
        .perform(
            post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }
}
