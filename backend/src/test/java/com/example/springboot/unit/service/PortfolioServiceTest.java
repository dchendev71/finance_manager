package com.example.springboot.unit.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springboot.config.TestConfig;
import com.example.springboot.helper.*;
import com.example.springboot.helper.EntityTestFactory;
import com.example.springboot.portfolio.PortfolioRepository;
import com.example.springboot.portfolio.PortfolioService;
import com.example.springboot.portfolio.mapper.*;
import com.example.springboot.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PortfolioServiceTest {
  @Mock private PortfolioRepository portfolioRepository;
  @Mock private PortfolioMapper portfolioMapper;
  @Mock private UserRepository userRepository;

  private PortfolioService portfolioService;

  @BeforeEach
  void setUp() {
    portfolioService = new PortfolioService(userRepository, portfolioRepository, portfolioMapper);
  }

  @Test
  @DisplayName("Should create a portfolio")
  void createPortfolio_should_succed() {
    when(userRepository.getByEmailOrThrow(TestConfig.User.email))
        .thenReturn(EntityTestFactory.UserFactory.create());
    when(portfolioMapper.toEntity(
            EntityTestFactory.UserFactory.create(), TestConfig.Portfolio.name))
        .thenReturn(EntityTestFactory.PortfolioFactory.create());

    portfolioService.createPortfolio(TestConfig.User.email, RequestTestFactory.Portfolio.create());

    verify(portfolioRepository).save(any());
  }
}
