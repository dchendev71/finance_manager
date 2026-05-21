package com.example.springboot.helper;

import com.example.springboot.auth.dto.AuthRequest;
import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.config.TestConfig;
import com.example.springboot.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.MockMvc;

public class HelpSetup {
  private RequestHandler requestHandler;

  public HelpSetup(MockMvc mockMvc, ObjectMapper objectMapper) {

    this.requestHandler = new RequestHandler(mockMvc, objectMapper);
  }

  public User registerUser(String email) throws Exception {
    User user = EntityTestFactory.UserFactory.create(email);
    UserCreateRequest request =
        RequestTestFactory.User.register(email, TestConfig.User.password, TestConfig.Currency.code);
    requestHandler.performPost(ApiRoutes.Auth.REGISTER, request);

    return user;
  }

  public String registerUserAndLogin(String email) throws Exception {
    registerUser(email);

    AuthRequest request = new AuthRequest(email, TestConfig.User.password);
    String response =
        requestHandler
            .performPost(ApiRoutes.Auth.LOGIN, request)
            .andReturn()
            .getResponse()
            .getContentAsString();

    return JsonPath.read(response, "$.jwtToken");
  }
}
