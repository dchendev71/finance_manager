package com.example.springboot.helper;

import com.example.springboot.auth.dto.AuthRequest;
import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.balance.dto.UserBalanceRequest;
import com.example.springboot.common.config.ApiRoutes;
import com.example.springboot.config.TestConfig;
import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.asset.Asset;
import com.example.springboot.portfolio.asset.AssetRepository;
import com.example.springboot.portfolio.dto.PortfolioCreateRequest;
import com.example.springboot.portfolio.portfolio_asset.PortfolioAsset;
import com.example.springboot.portfolio.portfolio_asset.dto.PortfolioAssetRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

public class TestSetup {
  @Autowired AssetRepository assetRepository;
  private RequestHandler requestHandler;
  public TestSetupDetails testSetupDetails;

  public TestSetup(MockMvc mockMvc, ObjectMapper objectMapper) {
    this.requestHandler = new RequestHandler(mockMvc, objectMapper);
    this.testSetupDetails = new TestSetupDetails();
  }

  public TestSetup(MockMvc mockMvc, ObjectMapper objectMapper, AssetRepository assetRepository) {
    this.requestHandler = new RequestHandler(mockMvc, objectMapper);
    this.testSetupDetails = new TestSetupDetails();
    this.assetRepository = assetRepository;
  }

  public ResultActions registerUser(String email) throws Exception {
    this.testSetupDetails.setUser(EntityTestFactory.UserFactory.create(email));
    UserCreateRequest request =
        RequestTestFactory.User.register(email, TestConfig.User.password, TestConfig.Currency.code);
    return requestHandler.performPost(ApiRoutes.Auth.REGISTER, request);
  }

  public ResultActions increaseUserBalance(String jwtToken) throws Exception {
    UserBalanceRequest request = RequestTestFactory.UserBalance.create();
    return requestHandler.performAuthorizedRequest(
        ApiRoutes.UserBalance.INCREASE, request, HttpMethod.POST, jwtToken);
  }

  public ResultActions registerUserAndLogin(String email) throws Exception {
    registerUser(email);

    AuthRequest request = new AuthRequest(email, TestConfig.User.password);
    ResultActions resultAction = requestHandler.performPost(ApiRoutes.Auth.LOGIN, request);
    String response = resultAction.andReturn().getResponse().getContentAsString();

    this.testSetupDetails.setJwtToken(JsonPath.read(response, "$.jwtToken"));

    return resultAction;
  }

  public ResultActions createPortfolio(String portfolioName, String jwtToken) throws Exception {
    PortfolioCreateRequest request = RequestTestFactory.Portfolio.create(portfolioName);
    ResultActions resultAction =
        requestHandler.performAuthorizedRequest(
            ApiRoutes.Portfolios.CREATE, request, HttpMethod.POST, jwtToken);
    Portfolio portfolio = EntityTestFactory.PortfolioFactory.create(portfolioName);
    this.testSetupDetails.setPortfolio(portfolio);

    return resultAction;
  }

  public ResultActions createPortfolioAsset(
      Portfolio portfolio, String assetName, BigDecimal quantity, String jwtToken)
      throws Exception {
    PortfolioAssetRequest request = RequestTestFactory.PortfolioAsset.create(assetName, quantity);

    String route = ApiRoutes.Portfolios.PortfolioAssets.BASE + "/" + portfolio.getName();

    ResultActions resultActions =
        requestHandler.performAuthorizedRequest(route, request, HttpMethod.POST, jwtToken);

    Asset asset = assetRepository.getByNameOrThrow(assetName);

    PortfolioAsset portfolioAsset =
        EntityTestFactory.PortfolioAssetFactory.create(portfolio, asset, quantity);

    this.testSetupDetails.getPortfolioAssetList().add(portfolioAsset);
    return resultActions;
  }
}
