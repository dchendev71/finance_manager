package com.example.springboot.portfolio.portfolio_asset;

import com.example.springboot.portfolio.PortfolioRepository;
import com.example.springboot.portfolio.portfolio_asset.dto.CreatePortfolioAssetRequest;
import com.example.springboot.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PortfolioAssetService {
  private final PortfolioAssetRepository portfolioAssetRepository;
  private final UserRepository userRepository;
  private final PortfolioRepository portfolioRepository;

  @Autowired
  public PortfolioAssetService(
      PortfolioAssetRepository portfolioAssetRepository,
      UserRepository userRepository,
      PortfolioRepository portfolioRepository) {
    this.portfolioAssetRepository = portfolioAssetRepository;
    this.userRepository = userRepository;
    this.portfolioRepository = portfolioRepository;
  }

  public void createPortfolioAsset(String email, CreatePortfolioAssetRequest request) {
    // Check if user still exists
    // User user =
    //     userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    //
    // // Check if portfolio associated with the user exist
    // Portfolio portfolio =
    //     portfolioRepository
    //         .findByUserIdAndName(user.getId(), request.portfolioName())
    //         .orElseThrow(() -> new PortfolioNotFoundException(request.portfolioName()));
    //
    // // Check if portfolioAsset already exists - ie if we want to add quantity then use ADD
    // // endpoint
    // if (portfolioAssetRepository
    //     .findByAssetNameAndPortfolioId(request.assetName(), portfolio.getId())
    //     .isPresent()) {}
  }
}
