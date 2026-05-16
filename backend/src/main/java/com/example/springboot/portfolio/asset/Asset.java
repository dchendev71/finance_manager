package com.example.springboot.portfolio.asset;

import com.example.springboot.portfolio.asset_type.AssetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "asset")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode(of = "id")
public class Asset {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", length = 255, nullable = false)
  private String name;

  @Column(name = "ticker_symbol", length = 255, nullable = false)
  private String tickerSymbol;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "asset_type",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_asset_asset_type"))
  AssetType assetType;
}
