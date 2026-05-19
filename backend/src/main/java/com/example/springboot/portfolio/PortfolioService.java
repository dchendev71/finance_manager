package com.example.springboot.portfolio;

import com.example.springboot.common.exception.UserNotFoundException;
import com.example.springboot.portfolio.dto.PortfolioResponse;
import com.example.springboot.portfolio.mapper.PortfolioMapper;
import com.example.springboot.user.User;
import com.example.springboot.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {
  private final UserRepository userRepository;
  private final PortfolioRepository portfolioRepository;
  private final PortfolioMapper portfolioMapper;

  @Autowired
  public PortfolioService(
      PortfolioRepository portfolioRepository,
      PortfolioMapper portfolioMapper,
      UserRepository userRepository) {
    this.portfolioRepository = portfolioRepository;
    this.portfolioMapper = portfolioMapper;
    this.userRepository = userRepository;
  }

  public PortfolioResponse createPortfolio(String userEmail, String portfolioName) {
    User user =
        userRepository
            .findByEmail(userEmail)
            .orElseThrow(() -> new UserNotFoundException(userEmail));
    return portfolioMapper.toResponse(
        portfolioRepository.save(portfolioMapper.toEntity(user, portfolioName)));
  }
}
