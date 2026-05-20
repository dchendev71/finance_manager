package com.example.springboot.helper;

import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.dto.*;
import com.example.springboot.user.User;
import com.example.springboot.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class PortfolioTestFactory {
  public static String portfolioName = "newPortfolio";
  public static User user = UserTestFactory.createUser();

  @Autowired public static UserMapper userMapper;

  public static Portfolio createPortfolio() {
    return Portfolio.builder().name(portfolioName).user(user).build();
  }

  public static PortfolioResponse createPortfolioResponse() {
    return PortfolioResponse.builder()
        .portfolioName(portfolioName)
        .userResponse(UserTestFactory.createUserResponse())
        .build();
  }
}
