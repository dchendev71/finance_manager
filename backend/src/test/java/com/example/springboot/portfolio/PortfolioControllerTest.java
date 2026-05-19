package com.example.springboot.portfolio;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.helper.*;
import com.example.springboot.helper.RequestHandler;
import com.example.springboot.security.CustomUserDetailsService;
import com.example.springboot.security.JwtAccessDeniedHandler;
import com.example.springboot.security.JwtAuthenticationEntryPoint;
import com.example.springboot.security.JwtService;
import com.example.springboot.security.SecurityConfig;
import com.example.springboot.user.User;
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

@WebMvcTest(PortfolioController.class)
@Import({RequestHandler.class, SecurityConfig.class})
public class PortfolioControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  // Security filter chain

  @MockitoBean private JwtService jwtService;
  @MockitoBean private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  @MockitoBean private JwtAccessDeniedHandler jwtAccessDeniedHandler;
  @MockitoBean private CustomUserDetailsService customUserDetailsService;

  @MockitoBean private PortfolioService portfolioService;

  private RequestHandler requestHandler;
  private User user;

  @BeforeEach
  void setUp() {
    this.requestHandler = new RequestHandler(mockMvc, objectMapper);
    user = PortfolioTestFactory.user;
    // Enable mock Jwt
    JwtMockHelper.mockAuthorization(user, jwtService, customUserDetailsService);
  }

  @Test
  @DisplayName("POST /create-portfolio should return 200")
  void createPortfolio_shouldReturn200() throws Exception {

    when(portfolioService.createPortfolio(user.getEmail(), PortfolioTestFactory.portfolioName))
        .thenReturn(PortfolioTestFactory.createPortfolioResponse());

    requestHandler
        .performAuthorizedRequest(
            ApiRoutes.Portfolio.CREATE_PORTFOLIO,
            PortfolioTestFactory.portfolioName,
            HttpMethod.POST)
        .andExpect(status().isOk());
  }
}
