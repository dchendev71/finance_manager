package com.example.springboot.unit.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.helper.EntityTestFactory;
import com.example.springboot.helper.JwtMockHelper;
import com.example.springboot.helper.RequestHandler;
import com.example.springboot.helper.RequestTestFactory;
import com.example.springboot.helper.ResponseTestFactory;
import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.PortfolioController;
import com.example.springboot.portfolio.PortfolioService;
import com.example.springboot.portfolio.dto.PortfolioResponse;
import com.example.springboot.portfolio.mapper.PortfolioMapper;
import com.example.springboot.security.CustomUserDetailsService;
import com.example.springboot.security.JwtAccessDeniedHandler;
import com.example.springboot.security.JwtAuthenticationEntryPoint;
import com.example.springboot.security.JwtService;
import com.example.springboot.security.SecurityConfig;
import com.example.springboot.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
  @MockitoBean private PortfolioMapper portfolioMapper;

  private RequestHandler requestHandler;
  private User user;

  @BeforeEach
  void setUp() {
    this.requestHandler = new RequestHandler(mockMvc, objectMapper);
    this.user = EntityTestFactory.UserFactory.create();

    // Enable mock Jwt
    JwtMockHelper.mockAuthorization(user, jwtService, customUserDetailsService);
  }

  @Test
  @DisplayName("POST /create-portfolio should return 200")
  void createPortfolio_shouldReturn200() throws Exception {
    Portfolio portfolio = EntityTestFactory.PortfolioFactory.create();
    PortfolioResponse response = ResponseTestFactory.Portfolio.create();

    when(portfolioService.createPortfolio(user.getEmail(), RequestTestFactory.Portfolio.create()))
        .thenReturn(portfolio);
    when(portfolioMapper.toResponse(portfolio)).thenReturn(response);

    requestHandler
        .performAuthorizedRequest(
            ApiRoutes.Portfolios.CREATE, RequestTestFactory.Portfolio.create(), HttpMethod.POST)
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("GET /portfolios should return 200")
  void getPortfolios_shouldReturn200() throws Exception {
    PortfolioResponse response = ResponseTestFactory.Portfolio.create();
    User user = EntityTestFactory.UserFactory.create();
    List<Portfolio> portfolioList =
        List.of(
            Portfolio.builder().user(user).name("1").build(),
            Portfolio.builder().user(user).name("2").build());
    when(portfolioService.getPortfolios(user.getEmail())).thenReturn(portfolioList);
    when(portfolioMapper.toResponse(any())).thenReturn(response);

    requestHandler
        .performAuthorizedRequest(ApiRoutes.Portfolios.LIST, null, HttpMethod.GET)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(2)));
  }
}
