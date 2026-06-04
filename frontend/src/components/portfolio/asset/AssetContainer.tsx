import type { AssetRowData } from "./api";
import AssetRow from "./AssetRow";

export interface AssetContainerProps {
  assets: AssetRowData[];
}

export default function AssetContainer({ assets }: AssetContainerProps) {
  return (
    <>
      {assets.length > 0 &&
        assets.map((assetRow: AssetRowData) => (
          <AssetRow key={assetRow.asset.assetName} assetRow={assetRow} />
        ))}
    </>
  );
}
