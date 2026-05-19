package com.example.springboot.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springboot.auth.dto.AuthRequest;
import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.helper.AuthTestFactory;
import com.example.springboot.helper.RequestHandler;
import com.example.springboot.helper.UserTestFactory;
import com.example.springboot.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private UserRepository userRepository;

  private RequestHandler requestHandler;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    requestHandler = new RequestHandler(mockMvc, objectMapper);
  }

  @Nested
  class RegisterTests {
    @Test
    @DisplayName("An user should be able to register")
    void register_shouldReturn201() throws Exception {
      requestHandler
          .performPost(ApiRoutes.Auth.REGISTER, UserTestFactory.createUserRequest())
          .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("registration fail on invalid email")
    void register_shouldReturn401() throws Exception {
      UserCreateRequest request = new UserCreateRequest("not-an-email", "ValidPassword", "USD");
      requestHandler
          .performPost(ApiRoutes.Auth.REGISTER, request)
          .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("registration fail on password too short")
    void register_shouldReturn400_passwordTooShort() throws Exception {
      UserCreateRequest request = new UserCreateRequest("john124@gmail.com", "123", "USD");
      requestHandler
          .performPost(ApiRoutes.Auth.REGISTER, request)
          .andExpect(status().is4xxClientError());
    }
  }

  @Nested
  class LoginTests {
    @BeforeEach
    void setUp() throws Exception {
      // Register test user
      requestHandler.performPost(ApiRoutes.Auth.REGISTER, UserTestFactory.createUserRequest());
    }

    @Test
    @DisplayName("login should return a 200 and a jwtToken")
    void login_shouldReturn200() throws Exception {
      MvcResult result =
          requestHandler
              .performPost(ApiRoutes.Auth.LOGIN, AuthTestFactory.createAuthRequest())
              .andExpect(status().isOk())
              .andReturn();
      String response = result.getResponse().getContentAsString();
      String jwtToken = JsonPath.read(response, "$.jwtToken");
      // Check that JWT matches JWT token regex
      assertThat(jwtToken)
          .isNotBlank()
          .matches("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+$");
    }

    @Test
    @DisplayName("login should return 404, invalid crendentials")
    void login_shouldReturn404() throws Exception {
      AuthRequest request = new AuthRequest(AuthTestFactory.testEmail, "wrongPassword");
      requestHandler
          .performPost(ApiRoutes.Auth.LOGIN, request)
          .andExpect(status().is4xxClientError());
    }
  }
}
