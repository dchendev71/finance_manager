package com.example.springboot.unit.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.common.exception.NotFoundException;
import com.example.springboot.config.TestConfig;
import com.example.springboot.helper.EntityTestFactory;
import com.example.springboot.helper.JwtMockHelper;
import com.example.springboot.helper.RequestHandler;
import com.example.springboot.helper.RequestTestFactory;
import com.example.springboot.helper.ResponseTestFactory;
import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.portfolio_asset.PortfolioAssetController;
import com.example.springboot.portfolio.portfolio_asset.PortfolioAssetService;
import com.example.springboot.portfolio.portfolio_asset.dto.PortfolioAssetRequest;
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

@WebMvcTest(PortfolioAssetController.class)
@Import({RequestHandler.class, SecurityConfig.class})
public class PortfolioAssetControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  // Security Filter Chain
  @MockitoBean private JwtService jwtService;
  @MockitoBean private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  @MockitoBean private JwtAccessDeniedHandler jwtAccessDeniedHandler;
  @MockitoBean private CustomUserDetailsService customUserDetailsService;
  // Service
  @MockitoBean private PortfolioAssetService portfolioAssetService;

  private RequestHandler requestHandler;
  private User user;

  @BeforeEach
  void setUp() {
    this.requestHandler = new RequestHandler(mockMvc, objectMapper);
    this.user = EntityTestFactory.UserFactory.create();
    JwtMockHelper.mockAuthorization(user, jwtService, customUserDetailsService);
  }

  @Test
  @DisplayName("POST /portfolio-asset/create should return 200")
  void create_shouldReturn200() throws Exception {
    PortfolioAssetRequest request = RequestTestFactory.PortfolioAsset.create();
    when(portfolioAssetService.createPortfolioAsset(
            user.getEmail(), TestConfig.Portfolio.name, request))
        .thenReturn(ResponseTestFactory.PortfolioAsset.create());
    // PortfolioName is the targetted portfolio
    String route = ApiRoutes.Portfolios.PortfolioAssets.BASE + "/" + TestConfig.Portfolio.name;
    requestHandler
        .performAuthorizedRequest(route, request, HttpMethod.POST)
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("POST /portfolio-asset/create should return 4xx")
  void create_shouldReturn4xx() throws Exception {
    PortfolioAssetRequest request = RequestTestFactory.PortfolioAsset.create();
    when(portfolioAssetService.createPortfolioAsset(
            user.getEmail(), TestConfig.Portfolio.name, request))
        .thenThrow(new NotFoundException(Portfolio.class, TestConfig.Portfolio.name));

    String route = ApiRoutes.Portfolios.PortfolioAssets.BASE + "/" + TestConfig.Portfolio.name;
    requestHandler
        .performAuthorizedRequest(route, request, HttpMethod.POST)
        .andExpect(status().is4xxClientError());
  }
}
