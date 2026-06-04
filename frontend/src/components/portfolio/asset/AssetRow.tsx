import type { AssetRowData } from "./api";

export interface AssertRowProps {
  assetRow: AssetRowData;
}

export default function AssetRow({ assetRow }: AssertRowProps) {
  return (
    <h3>
      {assetRow.asset.assetName} {assetRow.asset.tickerSymbol}
      {assetRow.quantity} {assetRow.meanPrice}
    </h3>
  );
}
