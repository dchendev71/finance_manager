import Button from "@/components/ui/Button";
import type { AssetRowData } from "./api";
import Asset from "./Asset";
import { useState, type Dispatch, type SetStateAction } from "react";
import AssetForm, { type AssetMethod } from "./AssetForm";

export interface AssetRowProps {
  portfolioName: string;
  stateFn: Dispatch<SetStateAction<AssetRowData[]>>;
  assetRow: AssetRowData;
}

export default function AssetRow({
  portfolioName,
  stateFn,
  assetRow,
}: AssetRowProps) {
  const [formTitle, setFormTitle] = useState<string>("");
  const [displayAssetForm, setDisplayAssetForm] = useState<boolean>(false);
  const [assetMethod, setAssetMethod] = useState<AssetMethod>("BUY");
  const [submitValue, setSubmitValue] = useState<string>("");

  async function buyButton() {
    setFormTitle(`Buy ${assetRow.asset.assetName}`);
    setDisplayAssetForm(true);
    setAssetMethod("BUY");
    setSubmitValue("Buy");
  }

  async function sellButton() {
    setFormTitle(`Sell ${assetRow.asset.assetName}`);
    setDisplayAssetForm(true);
    setAssetMethod("SELL");
    setSubmitValue("Sell");
  }

  return (
    // Outer section wrapper
    <section className="w-full">
      <dl className="flex flex-row flex-wrap items-center gap-6 p-4 bg-slate-50 rounded-xl border border-slate-100 w-full">
        <div className="flex items-center gap-2">
          <Asset asset={assetRow.asset} />
        </div>

        <div>
          <dt className="text-xs text-slate-500">Quantity</dt>
          <dd className="text-sm font-semibold">{assetRow.quantity}</dd>
        </div>

        <div>
          <dt className="text-xs text-slate-500 font-medium">Mean Price</dt>
          <dd className="text-sm font-semibold">{assetRow.meanPrice}</dd>
        </div>

        {/* 'ml-auto' pushes this entire block to the far right */}
        <div className="ml-auto flex flex-row items-center gap-2">
          <Button value="Buy" variant="green" onClick={buyButton} />
          <Button value="Sell" variant="red" onClick={sellButton} />
        </div>
      </dl>

      {displayAssetForm && (
        <AssetForm
          formProps={{
            formTitle: formTitle,
            cancelForm: () => setDisplayAssetForm(false),
            submitValue: submitValue,
          }}
          assetMethod={assetMethod}
          portfolioName={portfolioName}
          stateFn={stateFn}
        />
      )}
    </section>
  );
}
