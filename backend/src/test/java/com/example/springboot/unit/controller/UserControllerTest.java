package com.example.springboot.unit.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.common.exception.ExistsException;
import com.example.springboot.common.exception.InvalidCredentialsException;
import com.example.springboot.config.TestConfig;
import com.example.springboot.helper.EntityTestFactory;
import com.example.springboot.helper.JwtMockHelper;
import com.example.springboot.helper.RequestHandler;
import com.example.springboot.helper.RequestTestFactory;
import com.example.springboot.helper.ResponseTestFactory;
import com.example.springboot.security.CustomUserDetailsService;
import com.example.springboot.security.JwtAccessDeniedHandler;
import com.example.springboot.security.JwtAuthenticationEntryPoint;
import com.example.springboot.security.JwtService;
import com.example.springboot.security.SecurityConfig;
import com.example.springboot.user.User;
import com.example.springboot.user.UserController;
import com.example.springboot.user.UserService;
import com.example.springboot.user.dto.ChangeEmailRequest;
import com.example.springboot.user.dto.ChangePasswordRequest;
import com.example.springboot.user.dto.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@Import({RequestHandler.class, SecurityConfig.class})
public class UserControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  // Security Filter Chain
  @MockitoBean private JwtService jwtService;
  @MockitoBean private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  @MockitoBean private JwtAccessDeniedHandler jwtAccessDeniedHandler;
  @MockitoBean private CustomUserDetailsService customUserDetailsService;
  @MockitoBean private UserService userService;

  private RequestHandler requestHandler;
  // Usual Test Request, Test User and Test response
  private UserResponse userResponse;
  private User user;

  @BeforeEach
  void setUp() {
    this.requestHandler = new RequestHandler(mockMvc, objectMapper);
    this.userResponse = ResponseTestFactory.User.create();
    this.user = EntityTestFactory.UserFactory.create();
    JwtMockHelper.mockAuthorization(user, jwtService, customUserDetailsService);
  }

  @Test
  @DisplayName("POST /change-password should return 200")
  void changePassword_shouldReturn200() throws Exception {
    ChangePasswordRequest request = RequestTestFactory.User.changePassword("newPassword");

    when(userService.changePassword(TestConfig.User.email, request)).thenReturn(userResponse);

    requestHandler
        .performAuthorizedRequest(ApiRoutes.Users.CHANGE_PASSWORD, request, HttpMethod.POST)
        .andExpect(status().isOk());
    verify(userService).changePassword(TestConfig.User.email, request);
  }

  @Test
  @DisplayName("POST /change-password should return 401 Invalid Credentials")
  void changePassword_shouldReturn401() throws Exception {
    ChangePasswordRequest request =
        new ChangePasswordRequest("wrongPassword", "newPassword", "newPassword");

    doThrow(new InvalidCredentialsException())
        .when(userService)
        .changePassword(TestConfig.User.email, request);

    requestHandler
        .performAuthorizedRequest(ApiRoutes.Users.CHANGE_PASSWORD, request, HttpMethod.POST)
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("PUT /change-email should return 200")
  void changeEmail_shouldReturn200() throws Exception {
    ChangeEmailRequest request = RequestTestFactory.User.changeEmail("newtest@gmail.com");

    when(userService.changeEmail(TestConfig.User.email, request)).thenReturn(userResponse);

    requestHandler
        .performAuthorizedRequest(ApiRoutes.Users.CHANGE_EMAIL, request, HttpMethod.PUT)
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("PUT /change-email should return 409 email already exists")
  void changeEmail_shouldReturn409() throws Exception {
    ChangeEmailRequest request = RequestTestFactory.User.changeEmail("newtest@gmail.com");

    doThrow(new ExistsException(User.class, request.newEmail()))
        .when(userService)
        .changeEmail(TestConfig.User.email, request);
    requestHandler
        .performAuthorizedRequest(ApiRoutes.Users.CHANGE_EMAIL, request, HttpMethod.PUT)
        .andExpect(status().isConflict());
  }
}
