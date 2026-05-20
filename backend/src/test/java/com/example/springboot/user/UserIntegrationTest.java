package com.example.springboot.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.common.exception.ExistsException;
import com.example.springboot.common.exception.InvalidCredentialsException;
import com.example.springboot.helper.AuthTestFactory;
import com.example.springboot.helper.HelpSetup;
import com.example.springboot.helper.RequestHandler;
import com.example.springboot.helper.UserTestFactory;
import com.example.springboot.user.dto.ChangeEmailRequest;
import com.example.springboot.user.dto.ChangePasswordRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private UserRepository userRepository;

  private RequestHandler requestHandler;
  private String jwtToken;

  @BeforeEach
  void setUp() throws Exception {
    userRepository.deleteAll();
    requestHandler = new RequestHandler(mockMvc, objectMapper);

    HelpSetup helper = new HelpSetup(mockMvc, objectMapper);
    jwtToken = helper.registerUserAndLogin(UserTestFactory.testEmail);
  }

  @Nested
  class ChangePasswordTest {

    @Test
    @DisplayName("change password should return 200")
    void changePassword_shouldReturn200() throws Exception {
      User user = userRepository.findByEmail(UserTestFactory.testEmail).orElseThrow();
      String prevPassword = user.getPassword();
      requestHandler
          .performAuthorizedRequest(
              ApiRoutes.Users.CHANGE_PASSWORD,
              UserTestFactory.createChangePasswordRequest("newPassword"),
              HttpMethod.POST,
              jwtToken)
          .andExpect(status().isOk());

      // Check that the password did change
      String newPassword = user.getPassword();

      assertThat(!prevPassword.equals(newPassword));
    }

    @Test
    @DisplayName("change password should return 4xx InvalidCrendialsException")
    void changePassword_shouldReturn4xx() throws Exception {
      ChangePasswordRequest request =
          new ChangePasswordRequest("wrongCurrentPassword", "newPassword", "newPassword");
      requestHandler
          .performAuthorizedRequest(
              ApiRoutes.Users.CHANGE_PASSWORD, request, HttpMethod.POST, jwtToken)
          .andExpect(status().is4xxClientError())
          .andExpect(
              resp ->
                  assertTrue(resp.getResolvedException() instanceof InvalidCredentialsException));
    }
  }

  @Nested
  class ChangeEmailTest {

    @Test
    @DisplayName("change email should return 200")
    void changeEmail_shouldReturn200() throws Exception {
      User user = userRepository.findByEmail(UserTestFactory.testEmail).orElseThrow();
      String prevEmail = user.getEmail();

      requestHandler
          .performAuthorizedRequest(
              ApiRoutes.Users.CHANGE_EMAIL,
              UserTestFactory.createChangeEmailRequest("newEmail@gmail.com"),
              HttpMethod.PUT,
              jwtToken)
          .andExpect(status().isOk());
      String newEmail = user.getEmail();
      assertThat(!prevEmail.equals(newEmail));
      assertThat(newEmail.equals("newEmail@gmail.com"));
      assertThat(userRepository.findByEmail(prevEmail).isEmpty());
    }

    @Test
    @DisplayName("change email should return 4xx")
    void changeEmail_shouldReturn4xx() throws Exception {
      ChangeEmailRequest request =
          new ChangeEmailRequest("wrongCurrentPassword", "newEmail@gmail.com");

      requestHandler
          .performAuthorizedRequest(ApiRoutes.Users.CHANGE_EMAIL, request, HttpMethod.PUT, jwtToken)
          .andExpect(status().is4xxClientError())
          .andExpect(
              resp ->
                  assertTrue(resp.getResolvedException() instanceof InvalidCredentialsException));
    }

    @Test
    @DisplayName("change email should return 4xx when email already exists")
    void changeEmail_shouldReturn4xx_whenEmailExist() throws Exception {
      UserCreateRequest newUser = new UserCreateRequest("newEmail@gmail.com", "password", "USD");
      requestHandler.performPost(ApiRoutes.Auth.REGISTER, newUser);

      ChangeEmailRequest request =
          new ChangeEmailRequest(UserTestFactory.testPassword, "newEmail@gmail.com");

      requestHandler
          .performAuthorizedRequest(ApiRoutes.Users.CHANGE_EMAIL, request, HttpMethod.PUT, jwtToken)
          .andExpect(status().is4xxClientError())
          .andExpect(resp -> assertTrue(resp.getResolvedException() instanceof ExistsException));
    }
  }

  @Nested
  class DeleteUserTest {
    @Test
    @DisplayName("delete user should return 2xx")
    void deleteUser() throws Exception {
      requestHandler
          .performAuthorizedRequest(ApiRoutes.Users.DELETE, null, HttpMethod.DELETE, jwtToken)
          .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("delete user should not be able to call authorized route and return 4xx")
    void deleteUser_preventLogin() throws Exception {
      requestHandler.performAuthorizedRequest(
          ApiRoutes.Users.DELETE, null, HttpMethod.DELETE, jwtToken);

      // Should not be able to call authorized route
      requestHandler
          .performAuthorizedRequest(
              ApiRoutes.Users.CHANGE_PASSWORD,
              UserTestFactory.createChangePasswordRequest("newPassword"),
              HttpMethod.POST,
              jwtToken)
          .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Deleted user should not be able to get back a jwt Token")
    void deletedUser_shouldNotGetJwtToken() throws Exception {
      requestHandler.performAuthorizedRequest(
          ApiRoutes.Users.DELETE, null, HttpMethod.DELETE, jwtToken);

      requestHandler
          .performPost(ApiRoutes.Auth.LOGIN, AuthTestFactory.createAuthRequest())
          .andExpect(status().is4xxClientError());
    }
  }
}
