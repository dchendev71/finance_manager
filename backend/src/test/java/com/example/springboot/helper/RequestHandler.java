package com.example.springboot.helper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Function;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class RequestHandler {
  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  public RequestHandler(MockMvc mockMvc, ObjectMapper objectMapper) {
    this.mockMvc = mockMvc;
    this.objectMapper = objectMapper;
  }

  public ResultActions performPost(String route, Object request) throws Exception {
    return mockMvc.perform(
        post(route)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));
  }

  private MockHttpServletRequestBuilder buildRequest(HttpMethod httpMethod, String route)
      throws Exception {
    Function<String, MockHttpServletRequestBuilder> builderFunction =
        switch (httpMethod.name()) {
          case "GET" -> MockMvcRequestBuilders::get;
          case "POST" -> MockMvcRequestBuilders::post;
          case "PUT" -> MockMvcRequestBuilders::put;
          case "DELETE" -> MockMvcRequestBuilders::delete;
          case "PATCH" -> MockMvcRequestBuilders::patch;
          default ->
              throw new UnsupportedOperationException(
                  "HTTP Method not supported in test utility: " + httpMethod.name());
        };

    return builderFunction.apply(route);
  }

  public ResultActions performAuthorizedRequest(String route, Object request, HttpMethod httpMethod)
      throws Exception {
    return mockMvc.perform(
        buildRequest(httpMethod, route)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .header("Authorization", "Bearer " + JwtMockHelper.mockToken));
  }
}
