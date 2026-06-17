package com.example.springboot.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.springboot.asset.Asset;
import com.example.springboot.asset.AssetRepository;
import com.example.springboot.balance.UserBalanceService;
import com.example.springboot.common.exception.ExistsException;
import com.example.springboot.config.TestConfig;
import com.example.springboot.helper.EntityTestFactory;
import com.example.springboot.helper.RequestTestFactory;
import com.example.springboot.helper.ResponseTestFactory;
import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.PortfolioRepository;
import com.example.springboot.portfolio_asset.BalanceCheckerService;
import com.example.springboot.portfolio_asset.DataService;
import com.example.springboot.portfolio_asset.PortfolioAsset;
import com.example.springboot.portfolio_asset.PortfolioAssetRepository;
import com.example.springboot.portfolio_asset.PortfolioAssetService;
import com.example.springboot.portfolio_asset.dto.PortfolioAssetRequest;
import com.example.springboot.portfolio_asset.dto.PortfolioAssetResponse;
import com.example.springboot.portfolio_asset.mapper.PortfolioAssetMapper;
import com.example.springboot.portfolio_asset.mean_price.PortfolioAssetMeanPrice;
import com.example.springboot.portfolio_asset.mean_price.PortfolioAssetMeanPriceService;
import com.example.springboot.recorder.RecordService;
import com.example.springboot.transactions.TransactionsService;
import com.example.springboot.user.User;
import com.example.springboot.user.UserRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PortfolioAssetServiceTest {

  @Mock private RecordService recordService;
  @Mock private UserBalanceService userBalanceService;

  @Mock private UserRepository userRepository;
  @Mock private PortfolioRepository portfolioRepository;
  @Mock private AssetRepository assetRepository;
  @Mock private PortfolioAssetRepository portfolioAssetRepository;
  @Mock private PortfolioAssetMapper portfolioAssetMapper;
  @Mock private PortfolioAssetMeanPriceService portfolioAssetMeanPriceService;
  @Mock private TransactionsService transactionsService;
  @Mock private DataService dataService;
  @Mock private BalanceCheckerService balanceCheckerService;

  private User user;
  private Portfolio portfolio;
  private Asset asset;

  @InjectMocks PortfolioAssetService portfolioAssetService;

  @BeforeEach
  void setUp() {
    this.user = EntityTestFactory.UserFactory.create();
    this.portfolio = EntityTestFactory.PortfolioFactory.create();
    this.asset = EntityTestFactory.AssetFactory.create();
  }

  @Test
  @DisplayName("An user should be able to create a portfolio asset")
  public void createPortfolioAsset_shouldCreate() {
    PortfolioAssetRequest request = RequestTestFactory.PortfolioAsset.create();

    PortfolioAsset portfolioAsset = EntityTestFactory.PortfolioAssetFactory.create();
    PortfolioAssetMeanPrice meanPrice = EntityTestFactory.PortfolioAssetMeanPriceFactory.create();
    when(dataService.fetchData(user.getEmail(), portfolio.getName(), asset.getName()))
        .thenReturn(
            new DataService.Data(user, asset, portfolio, Optional.empty(), Optional.empty()));
    when(balanceCheckerService.performCheck(user, request))
        .thenReturn(
            new BalanceCheckerService.CheckedValues(
                true, new BigDecimal(1000), request.quantity(), TestConfig.PortfolioAsset.price));

    when(portfolioAssetMapper.toEntity(portfolio, asset, TestConfig.PortfolioAsset.quantity))
        .thenReturn(portfolioAsset);
    when(portfolioAssetRepository.save(any())).thenReturn(portfolioAsset);
    when(portfolioAssetMapper.toResponse(portfolioAsset, TestConfig.PortfolioAsset.meanPrice))
        .thenReturn(ResponseTestFactory.PortfolioAsset.create());
    when(portfolioAssetMeanPriceService.getMeanPriceOrThrow(portfolioAsset)).thenReturn(meanPrice);

    PortfolioAssetResponse response =
        portfolioAssetService.createPortfolioAsset(
            user.getEmail(), TestConfig.Portfolio.name, request);

    verify(portfolioAssetRepository).save(any());

    assertThat(response).isNotNull().isEqualTo(ResponseTestFactory.PortfolioAsset.create());
  }

  @Test
  @DisplayName(
      "An user should not be able to create the same portfolio asset even with different quantity")
  public void createPortfolioAsset_duplicate() {

    PortfolioAssetRequest request = RequestTestFactory.PortfolioAsset.create(new BigDecimal(999));

    PortfolioAsset portfolioAsset = EntityTestFactory.PortfolioAssetFactory.create();

    when(dataService.fetchData(user.getEmail(), portfolio.getName(), asset.getName()))
        .thenReturn(
            new DataService.Data(
                user, asset, portfolio, Optional.of(portfolioAsset), Optional.empty()));

    assertThrows(
        ExistsException.class,
        () ->
            portfolioAssetService.createPortfolioAsset(
                user.getEmail(), TestConfig.Portfolio.name, request));
  }
}
