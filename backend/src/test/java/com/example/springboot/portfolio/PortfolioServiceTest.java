package com.example.springboot.portfolio;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springboot.helper.*;
import com.example.springboot.portfolio.mapper.*;
import com.example.springboot.user.UserRepository;
import java.util.Optional;
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
    portfolioService = new PortfolioService(portfolioRepository, portfolioMapper, userRepository);
  }

  @Test
  @DisplayName("Should create a portfolio")
  void createPortfolio_should_succed() {
    when(userRepository.findByEmail(UserTestFactory.testEmail))
        .thenReturn(Optional.of(UserTestFactory.createUser()));
    when(portfolioMapper.toEntity(UserTestFactory.createUser(), PortfolioTestFactory.portfolioName))
        .thenReturn(PortfolioTestFactory.createPortfolio());

    portfolioService.createPortfolio(UserTestFactory.testEmail, PortfolioTestFactory.portfolioName);

    verify(portfolioRepository).save(any());
  }
}
