import type { AssetData } from "./api";

export interface AssetProps {
  asset: AssetData;
}

export default function Asset({ asset }: AssetProps) {
  return (
    <>
      <dl className="flex flex-row flex-wrap items-center gap-6 p-2 bg-slate-50">
        <div>
          <dt className="text-xs text-slate-500">Asset Name</dt>
          <dd className="text-sm font-semibold">{asset.assetName}</dd>
        </div>

        <div>
          <dt className="text-xs text-slate-500">Ticker Symbol</dt>
          <dd className="text-sm font-semibold">{asset.tickerSymbol}</dd>
        </div>
      </dl>
    </>
  );
}
