import type { Dispatch, SetStateAction } from "react";
import type { AssetRowData } from "./api";
import AssetRow from "./AssetRow";

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
