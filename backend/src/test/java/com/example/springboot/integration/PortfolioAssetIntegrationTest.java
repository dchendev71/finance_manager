package com.example.springboot.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.springboot.asset.AssetRepository;
import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.config.TestConfig;
import com.example.springboot.helper.RequestHandler;
import com.example.springboot.helper.RequestTestFactory;
import com.example.springboot.helper.TestSetup;
import com.example.springboot.portfolio_asset.PortfolioAssetRepository;
import com.example.springboot.portfolio_asset.dto.PortfolioAssetRequest;
import com.example.springboot.portfolio_asset.mean_price.PortfolioAssetMeanPriceService;
import com.example.springboot.price.PriceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class PortfolioAssetIntegrationTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private PortfolioAssetRepository portfolioAssetRepository;
  @Autowired private AssetRepository assetRepository;
  @Autowired private PortfolioAssetMeanPriceService portfolioAssetMeanPriceService;

  @MockitoBean private PriceService priceService;

  private RequestHandler requestHandler;
  private TestSetup testSetup;

  @BeforeEach
  void setUp() throws Exception {
    portfolioAssetRepository.deleteAll();
    requestHandler = new RequestHandler(mockMvc, objectMapper);
    testSetup = new TestSetup(mockMvc, objectMapper, assetRepository);

    testSetup.registerUserAndLogin(TestConfig.User.email);
    testSetup.increaseUserBalance(testSetup.testSetupDetails.getJwtToken());
    testSetup.createPortfolio(TestConfig.Portfolio.name, testSetup.testSetupDetails.getJwtToken());
  }

  @Test
  @DisplayName("Should create a portfolioAsset happy ending")
  void create_shouldReturn200() throws Exception {
    PortfolioAssetRequest request = RequestTestFactory.PortfolioAsset.create();
    String route = ApiRoutes.Portfolios.PortfolioAssets.BASE + "/" + TestConfig.Portfolio.name;

    when(priceService.getIndicativePrice(TestConfig.Asset.tickerSymbol))
        .thenReturn(TestConfig.PortfolioAsset.price.doubleValue());
    ResultActions resultActions =
        requestHandler.performAuthorizedRequest(
            ApiRoutes.Portfolios.PortfolioAssets.BASE + "/" + TestConfig.Portfolio.name,
            request,
            HttpMethod.POST,
            testSetup.testSetupDetails.getJwtToken());
    resultActions.andExpect(status().isCreated());
    // Check meanPrice we need to get it as string first, and becompare it as BigDecimal
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    String rawAvgPrice = JsonPath.read(responseBody, "$.meanPrice");

    BigDecimal actualMean = new BigDecimal(rawAvgPrice);
    BigDecimal expectedMean = TestConfig.PortfolioAsset.meanPrice;

    assertThat(actualMean).isCloseTo(expectedMean, within(new BigDecimal("0.01")));
  }

  @Test
  @DisplayName("Should have the right meanPrice when updating the same asset")
  void update_shouldReturn200() throws Exception {
    PortfolioAssetRequest request =
        new PortfolioAssetRequest("BITCOIN", new BigDecimal(10), new BigDecimal(10));
    when(priceService.getIndicativePrice("BTC")).thenReturn(10.0);
    String route = ApiRoutes.Portfolios.PortfolioAssets.BASE + "/" + TestConfig.Portfolio.name;
    requestHandler
        .performAuthorizedRequest(
            route, request, HttpMethod.POST, testSetup.testSetupDetails.getJwtToken())
        .andExpect(status().isCreated());

    PortfolioAssetRequest updateRequest =
        new PortfolioAssetRequest("BITCOIN", new BigDecimal(10), new BigDecimal(20));

    when(priceService.getIndicativePrice("BTC")).thenReturn(20.0);
    ResultActions resultActions =
        requestHandler
            .performAuthorizedRequest(
                route, updateRequest, HttpMethod.PATCH, testSetup.testSetupDetails.getJwtToken())
            .andExpect(status().isOk());
    String responseBody = resultActions.andReturn().getResponse().getContentAsString();
    String rawAvgPrice = JsonPath.read(responseBody, "$.meanPrice");

    BigDecimal actualMean = new BigDecimal(rawAvgPrice);
    BigDecimal expectedMean = new BigDecimal(15);

    assertThat(actualMean).isCloseTo(expectedMean, within(new BigDecimal("0.01")));
  }

  @Test
  @DisplayName("Should not be able to create the same portfolio asset")
  void create_shouldReturn4xx() throws Exception {
    // Create portfolioAsset
    when(priceService.getIndicativePrice(TestConfig.Asset.name))
        .thenReturn(TestConfig.PortfolioAsset.price.doubleValue());
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

  @Test
  @DisplayName("Should be able to create a portfolio asset, and update it leading to its deletion")
  void updateTillDeletion_shouldReturn204() throws Exception {
    PortfolioAssetRequest request =
        new PortfolioAssetRequest("BITCOIN", new BigDecimal(10), new BigDecimal(10));
    when(priceService.getIndicativePrice("BTC")).thenReturn(10.0);
    String route = ApiRoutes.Portfolios.PortfolioAssets.BASE + "/" + TestConfig.Portfolio.name;
    requestHandler
        .performAuthorizedRequest(
            route, request, HttpMethod.POST, testSetup.testSetupDetails.getJwtToken())
        .andExpect(status().isCreated());

    PortfolioAssetRequest updateRequest =
        new PortfolioAssetRequest("BITCOIN", new BigDecimal(-10), new BigDecimal(20));
    when(priceService.getIndicativePrice("BTC")).thenReturn(20.0);
    ResultActions resultActions =
        requestHandler
            .performAuthorizedRequest(
                route, updateRequest, HttpMethod.PATCH, testSetup.testSetupDetails.getJwtToken())
            .andExpect(status().isNoContent());
  }
}
