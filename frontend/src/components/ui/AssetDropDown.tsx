import { useEffect, useState } from "react";
import { responseToAssetData, type AssetData } from "@/components/asset/api";
import { useAuth } from "@/components/auth/AuthContext";
import FormErrorBanner from "./FormErrorBanner";
import InputField from "./InputField";

export default function AssetDropDown() {
  const [assets, setAssets] = useState<AssetData[]>([]);
  const { request } = useAuth();
  const [error, setError] = useState<string | null>(null);

  const [selectedSymbol, setSelectedSymbol] = useState<string | null>(null);

  const [price, setPrice] = useState<number | null>(null);

  // Fetch Asset
  useEffect(() => {
    async function fetchAssets() {
      try {
        const response: any[] = await request("/assets");
        const mapped = response.map(responseToAssetData);
        if (mapped.length > 0) {
          setSelectedSymbol(mapped[0].tickerSymbol);
        }

        setAssets(mapped);
      } catch (e: any) {
        setError(e.message || "Network error - please try again");
      }
    }

    fetchAssets();
  }, []);

  // Fetch selected symbol price
  useEffect(() => {
    if (!selectedSymbol) {
      return;
    }
    async function fetchPrice() {
      const response = await request(`/price/${selectedSymbol}`);
      setPrice(response);
    }

    fetchPrice();
  }, [selectedSymbol]);

  return (
    <>
      <FormErrorBanner message={error} />
      <section>
        <div className="relative w-full">
          <select
            onChange={(e) => setSelectedSymbol(e.target.value)}
            id="assetName"
            name="assetName"
            required
            className="w-full h-12 sm:h-10 pl-3 pr-10 py-2 bg-white border border-slate-300 rounded-lg text-base sm:text-sm appearance-none focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all shadow-sm cursor-pointer text-slate-900 font-medium"
          >
            {assets.map((asset) => (
              <option id={asset.tickerSymbol} value={asset.tickerSymbol}>
                {asset.assetName} ({asset.tickerSymbol})
              </option>
            ))}
          </select>

          <div className="absolute inset-y-0 right-0 flex items-center pr-3 pointer-events-none text-slate-500">
            <svg
              className="h-5 w-5"
              viewBox="0 0 20 20"
              fill="currentColor"
              aria-hidden="true"
            >
              <path
                fillRule="evenodd"
                d="M5.23 7.21a.75.75 0 011.06.02L10 11.168l3.71-3.938a.75.75 0 111.08 1.04l-4.25 4.5a.75.75 0 01-1.08 0l-4.25-4.5a.75.75 0 01.02-1.06z"
                clipRule="evenodd"
              />
            </svg>
          </div>
        </div>
      </section>
      <InputField
        id="unitPrice"
        label="Unit Price"
        type="number"
        value={price?.toString()}
        readOnly={true}
      />
    </>
  );
}
