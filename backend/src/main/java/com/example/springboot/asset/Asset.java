package com.example.springboot.asset;

import com.example.springboot.asset_type.AssetType;
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
@Table(name = "assets")
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

  // FIXME: FetchType.Lazy break RedisConfig, so we will need later to return a DTO rather than the
  // Asset
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(
      name = "asset_types",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_assets_asset_types"))
  AssetType assetType;
}
