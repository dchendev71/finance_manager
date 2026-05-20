package com.example.springboot.portfolio;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.common.exception.ExistsException;
import com.example.springboot.helper.HelpSetup;
import com.example.springboot.helper.PortfolioTestFactory;
import com.example.springboot.helper.RequestHandler;
import com.example.springboot.helper.UserTestFactory;
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
    jwtToken = new HelpSetup(mockMvc, objectMapper).registerUserAndLogin(UserTestFactory.testEmail);
  }

  @Nested
  class CreatePortfolioTest {
    @Test
    @DisplayName("An user should be able to create a portfolio")
    void createPortfolio() throws Exception {
      requestHandler
          .performAuthorizedRequest(
              ApiRoutes.Portfolio.CREATE_PORTFOLIO,
              PortfolioTestFactory.portfolioName,
              HttpMethod.POST,
              jwtToken)
          .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("An user should not be able to create the same portfolio name")
    void createSamePortfolio() throws Exception {

      requestHandler.performAuthorizedRequest(
          ApiRoutes.Portfolio.CREATE_PORTFOLIO,
          PortfolioTestFactory.portfolioName,
          HttpMethod.POST,
          jwtToken);

      requestHandler
          .performAuthorizedRequest(
              ApiRoutes.Portfolio.CREATE_PORTFOLIO,
              PortfolioTestFactory.portfolioName,
              HttpMethod.POST,
              jwtToken)
          .andExpect(status().is4xxClientError())
          .andExpect(resp -> assertTrue(resp.getResolvedException() instanceof ExistsException));
    }
  }
}
