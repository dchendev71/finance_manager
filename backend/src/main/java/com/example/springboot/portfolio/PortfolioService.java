package com.example.springboot.portfolio;

import com.example.springboot.common.exception.ExistsException;
import com.example.springboot.portfolio.dto.PortfolioCreateRequest;
import com.example.springboot.portfolio.dto.PortfolioResponse;
import com.example.springboot.portfolio.mapper.PortfolioMapper;
import com.example.springboot.user.User;
import com.example.springboot.user.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioService {
  private final UserRepository userRepository;
  private final PortfolioRepository portfolioRepository;
  private final PortfolioMapper portfolioMapper;

  @Transactional
  public PortfolioResponse createPortfolio(String userEmail, PortfolioCreateRequest request) {
    // Check if users still exists
    User user = userRepository.getByEmailOrThrow(userEmail);
    // Check if protfolio with same name and same user exists
    if (portfolioRepository
        .findByUserIdAndName(user.getId(), request.portfolioName())
        .isPresent()) {
      throw new ExistsException(Portfolio.class, request.portfolioName());
    }
    return portfolioMapper.toResponse(
        portfolioRepository.save(portfolioMapper.toEntity(user, request.portfolioName())));
  }

  // TODO: test function
  public List<PortfolioResponse> getPortfolios(String userEmail) {
    // Check if user still exists
    User user = userRepository.getByEmailOrThrow(userEmail);
    List<Portfolio> portfolios = portfolioRepository.findAllByUserId(user.getId());

    return portfolios.stream().map(portfolioMapper::toResponse).toList();
  }
}
