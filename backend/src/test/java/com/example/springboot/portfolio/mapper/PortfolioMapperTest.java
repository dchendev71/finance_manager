package com.example.springboot.portfolio.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springboot.currency.mapper.*;
import com.example.springboot.helper.*;
import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.dto.PortfolioResponse;
import com.example.springboot.user.User;
import com.example.springboot.user.mapper.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import({UserMapperImpl.class, PortfolioMapperImpl.class, CurrencyMapperImpl.class})
class PortfolioMapperTest {

  @Autowired private PortfolioMapper portfolioMapper;

  @Test
  @DisplayName("toEntity: should create a Portfolio")
  void toEntity_shouldCreatePortfolio() {
    User user = UserTestFactory.createUser();

    Portfolio portfolio = portfolioMapper.toEntity(user, PortfolioTestFactory.portfolioName);
    assertThat(portfolio).isNotNull();
    assertThat(portfolio.getName()).isEqualTo(PortfolioTestFactory.portfolioName);
    assertThat(portfolio.getUser().getEmail()).isEqualTo(UserTestFactory.testEmail);
  }

  @Test
  @DisplayName("toResponse: should correctly map Portfolio to PortfolioResponse")
  void toResponse_shouldMapAllField() {
    Portfolio portfolio = PortfolioTestFactory.createPortfolio();

    PortfolioResponse response = portfolioMapper.toResponse(portfolio);
    assertThat(response).isNotNull();
    assertThat(response.portfolioName()).isEqualTo(PortfolioTestFactory.portfolioName);
    assertThat(response.userResponse().email()).isEqualTo(UserTestFactory.testEmail);
  }
}
