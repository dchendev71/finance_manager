package com.example.springboot.helper;

import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio_asset.PortfolioAsset;
import com.example.springboot.user.User;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestSetupDetails {
  private User user;
  private Portfolio portfolio;
  private String jwtToken;
  private List<PortfolioAsset> portfolioAssetList;

  TestSetupDetails() {
    this.portfolioAssetList = new ArrayList<>();
  }
}
