package com.example.springboot.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.common.exception.ExistsException;
import com.example.springboot.config.TestConfig;
import com.example.springboot.helper.HelpSetup;
import com.example.springboot.helper.RequestHandler;
import com.example.springboot.helper.RequestTestFactory;
import com.example.springboot.portfolio.PortfolioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class PortfolioIntegrationTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private PortfolioRepository portfolioRepository;

  private RequestHandler requestHandler;
  private String jwtToken;

  @BeforeEach
  void setUp() throws Exception {
    portfolioRepository.deleteAll();
    requestHandler = new RequestHandler(mockMvc, objectMapper);
    jwtToken = new HelpSetup(mockMvc, objectMapper).registerUserAndLogin(TestConfig.User.email);
  }

  @Nested
  class CreatePortfolioTest {
    @Test
    @DisplayName("An user should be able to create a portfolio")
    void createPortfolio() throws Exception {
      requestHandler
          .performAuthorizedRequest(
              ApiRoutes.Portfolio.CREATE_PORTFOLIO,
              RequestTestFactory.Portfolio.create(),
              HttpMethod.POST,
              jwtToken)
          .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("An user should not be able to create the same portfolio name")
    void createSamePortfolio() throws Exception {

      requestHandler.performAuthorizedRequest(
          ApiRoutes.Portfolio.CREATE_PORTFOLIO,
          RequestTestFactory.Portfolio.create(),
          HttpMethod.POST,
          jwtToken);

      requestHandler
          .performAuthorizedRequest(
              ApiRoutes.Portfolio.CREATE_PORTFOLIO,
              RequestTestFactory.Portfolio.create(),
              HttpMethod.POST,
              jwtToken)
          .andExpect(status().is4xxClientError())
          .andExpect(resp -> assertTrue(resp.getResolvedException() instanceof ExistsException));
    }
  }
}
