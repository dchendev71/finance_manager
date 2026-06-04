import BlueButton from "@/components/ui/BlueButton";
import type { AssetRowData } from "./api";

export interface AssertRowProps {
  assetRow: AssetRowData;
}

// TODO: Link button to API endpoint
export default function AssetRow({ assetRow }: AssertRowProps) {
  return (
    <h3>
      {assetRow.asset.assetName} {assetRow.asset.tickerSymbol}
      {assetRow.quantity} {assetRow.meanPrice}
      <BlueButton value="Buy" />
      <BlueButton value="Sell" />
    </h3>
  );
}
