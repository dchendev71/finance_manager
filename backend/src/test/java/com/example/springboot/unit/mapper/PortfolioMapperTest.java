package com.example.springboot.unit.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springboot.config.TestConfig;
import com.example.springboot.currency.mapper.*;
import com.example.springboot.helper.*;
import com.example.springboot.helper.EntityTestFactory;
import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.dto.PortfolioResponse;
import com.example.springboot.portfolio.mapper.PortfolioMapper;
import com.example.springboot.portfolio.mapper.PortfolioMapperImpl;
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
    User user = EntityTestFactory.UserFactory.create();

    Portfolio portfolio = portfolioMapper.toEntity(user, TestConfig.Portfolio.name);
    assertThat(portfolio).isNotNull();
    assertThat(portfolio.getName()).isEqualTo(TestConfig.Portfolio.name);
    assertThat(portfolio.getUser().getEmail()).isEqualTo(TestConfig.User.email);
  }

  @Test
  @DisplayName("toResponse: should correctly map Portfolio to PortfolioResponse")
  void toResponse_shouldMapAllField() {
    Portfolio portfolio = EntityTestFactory.PortfolioFactory.create();

    PortfolioResponse response = portfolioMapper.toResponse(portfolio);
    assertThat(response).isNotNull();
    assertThat(response.portfolioName()).isEqualTo(TestConfig.Portfolio.name);
    assertThat(response.userResponse().email()).isEqualTo(TestConfig.User.email);
  }
}
