package com.example.springboot.helper;

import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.dto.*;
import com.example.springboot.user.User;

public class PortfolioTestFactory {
  public static String portfolioName = "newPortfolio";
  public static User user = UserTestFactory.createUser();

  public static Portfolio createPortfolio() {
    return Portfolio.builder().name(portfolioName).user(user).build();
  }

  public static PortfolioResponse createPortfolioResponse() {
    return new PortfolioResponse(portfolioName, user);
  }
}
