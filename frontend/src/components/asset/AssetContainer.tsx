import { useEffect, type Dispatch, type SetStateAction } from "react";
import type { AssetRowData } from "./api";
import AssetRow from "./AssetRow";
import { useAssetPrice } from "./AssetPriceContext";

export interface AssetContainerProps {
  assets: AssetRowData[];
  stateFn: Dispatch<SetStateAction<AssetRowData[]>>;
  portfolioName: string;
}

export default function AssetContainer({
  portfolioName,
  stateFn,
  assets,
}: AssetContainerProps) {
  const { subscribe } = useAssetPrice();
  useEffect(() => {
    const symbols = assets.map((asset) => asset.asset.tickerSymbol);
    subscribe(symbols);
  }, [assets]);
  return (
    <>
      {assets.length > 0 &&
        assets.map((assetRow: AssetRowData) => (
          <AssetRow
            key={assetRow.asset.assetName}
            portfolioName={portfolioName}
            stateFn={stateFn}
            assetRow={assetRow}
          />
        ))}
    </>
  );
}
