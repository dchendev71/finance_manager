package com.example.springboot.helper;

import com.example.springboot.portfolio.Portfolio;

public class PortfolioTestFactory {
  public static String portfolioName = "newPortfolio";

  public static Portfolio createPortfolio() {
    return Portfolio.builder().name(portfolioName).user(UserTestFactory.createUser()).build();
  }
}
