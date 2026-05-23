package com.example.springboot.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.config.TestConfig;
import com.example.springboot.helper.RequestHandler;
import com.example.springboot.helper.RequestTestFactory;
import com.example.springboot.helper.TestSetup;
import com.example.springboot.portfolio.asset.AssetRepository;
import com.example.springboot.portfolio.portfolio_asset.PortfolioAssetRepository;
import com.example.springboot.portfolio.portfolio_asset.dto.CreatePortfolioAssetRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class PortfolioAssetIntegrationTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private PortfolioAssetRepository portfolioAssetRepository;
  @Autowired private AssetRepository assetRepository;

  private RequestHandler requestHandler;
  private TestSetup testSetup;

  @BeforeEach
  void setUp() throws Exception {
    portfolioAssetRepository.deleteAll();
    requestHandler = new RequestHandler(mockMvc, objectMapper);
    testSetup = new TestSetup(mockMvc, objectMapper, assetRepository);

    testSetup.registerUserAndLogin(TestConfig.User.email);
    testSetup.createPortfolio(TestConfig.Portfolio.name, testSetup.testSetupDetails.getJwtToken());
  }

  @Test
  @DisplayName("Should create a portfolioAsset happy ending")
  void create_shouldReturn200() throws Exception {
    CreatePortfolioAssetRequest request = RequestTestFactory.PortfolioAsset.create();
    requestHandler
        .performAuthorizedRequest(
            ApiRoutes.Portfolio.PortfolioAsset.CREATE_PORTFOLIO_ASSET,
            request,
            HttpMethod.POST,
            testSetup.testSetupDetails.getJwtToken())
        .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("Should not be able to create the same portfolio asset")
  void create_shouldReturn4xx() throws Exception {
    // Create portfolioAsset
    testSetup.createPortfolioAsset(
        testSetup.testSetupDetails.getPortfolio(),
        TestConfig.Asset.name,
        TestConfig.PortfolioAsset.quantity,
        testSetup.testSetupDetails.getJwtToken());

    // Create on same user, same portfolio, different quantity
    testSetup
        .createPortfolioAsset(
            testSetup.testSetupDetails.getPortfolio(),
            TestConfig.Asset.name,
            new BigDecimal(100000),
            testSetup.testSetupDetails.getJwtToken())
        .andExpect(status().is4xxClientError());
  }
}
