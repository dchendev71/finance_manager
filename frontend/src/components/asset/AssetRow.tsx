import Button from "@/components/ui/Button";
import type { AssetRowData } from "./api";
import Asset from "./Asset";
import { useEffect, useState, type Dispatch, type SetStateAction } from "react";
import AssetForm, { type AssetMethod } from "./AssetForm";
import FormErrorBanner from "@/components/ui/FormErrorBanner";
import { useAssetPrice } from "./AssetPriceContext";

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
  const [error, setError] = useState<string | null>(null);
  const [formTitle, setFormTitle] = useState<string>("");
  const [displayAssetForm, setDisplayAssetForm] = useState<boolean>(false);
  const [assetMethod, setAssetMethod] = useState<AssetMethod>("BUY");
  const [submitValue, setSubmitValue] = useState<string>("");

  const [assetPrice, setAssetPrice] = useState<number | null>(null);
  const { subscriptionsRef } = useAssetPrice();

  useEffect(() => {
    const interval = setInterval(() => {
      const price = subscriptionsRef.current.get(
        assetRow.asset.tickerSymbol,
      )?.price;
      if (price !== undefined) {
        setAssetPrice(price);
      }
    }, 2000);

    return () => clearInterval(interval);
  }, []);

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

  const formatNumber = (value: number, decimals = 2): string =>
    new Intl.NumberFormat("fr-FR", {
      minimumFractionDigits: decimals,
      maximumFractionDigits: decimals,
    }).format(value);

  return (
    // Outer section wrapper
    <section className="w-full">
      <dl className="flex flex-row flex-wrap items-center gap-6 p-4 bg-slate-50 rounded-xl border border-slate-100 w-full">
        <FormErrorBanner message={error} />
        <div className="flex items-center gap-2">
          <Asset asset={assetRow.asset} />
        </div>

        <div>
          <dt className="text-xs text-slate-500">Quantity</dt>
          <dd className="text-sm font-semibold">
            {formatNumber(Number(assetRow.quantity))}
          </dd>
        </div>

        <div>
          <dt className="text-xs text-slate-500 font-medium">Mean Price</dt>
          <dd className="text-sm font-semibold">
            {formatNumber(Number(assetRow.meanPrice))}
          </dd>
        </div>

        {assetPrice !== null && (
          <div>
            <dt className="text-xs text-slate-500 font-medium">
              Current Price
            </dt>
            <dd className="text-sm font-semibold">
              {formatNumber(assetPrice)}
            </dd>
          </div>
        )}

        {/* 'ml-auto' pushes this entire block to the far right */}
        <div className="ml-auto flex flex-row items-center gap-2">
          <Button value="Buy" variant="green" onClick={buyButton} />
          <Button value="Sell" variant="red" onClick={sellButton} />
        </div>
      </dl>

      {displayAssetForm && assetPrice !== null && (
        <AssetForm
          formProps={{
            formTitle: formTitle,
            cancelForm: () => setDisplayAssetForm(false),
            submitValue: submitValue,
            defaultValue: assetRow.asset.assetName,
            disabled: true,
            assetPrice: assetPrice as number,
          }}
          assetMethod={assetMethod}
          portfolioName={portfolioName}
          stateFn={stateFn}
          setError={setError}
        />
      )}
    </section>
  );
}
