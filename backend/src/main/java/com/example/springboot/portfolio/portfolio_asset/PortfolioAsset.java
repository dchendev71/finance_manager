package com.example.springboot.portfolio.portfolio_asset;

import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.asset.Asset;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "portfolio_asset",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uk_portfolio_asset_combination",
            columnNames = {"portfolio_id", "asset_id"}))
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class PortfolioAsset {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(
      name = "portfolio_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_portfolio_asset_portfolio"))
  private Portfolio portfolio;

  @ManyToOne
  @JoinColumn(
      name = "asset_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_portfolio_asset_asset"))
  private Asset asset;

  @Column(name = "quantity", nullable = false)
  private BigDecimal quantity;
}
