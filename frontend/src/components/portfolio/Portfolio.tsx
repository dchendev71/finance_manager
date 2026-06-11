import { useEffect, useState } from "react";
import { useAuth } from "@/components/auth/AuthContext";
import { getAssets, type AssetRowData } from "./asset/api";
import AssetForm, { type FormProps } from "./asset/AssetForm";
import AssetContainer from "./asset/AssetContainer";
import Button from "@/components/ui/Button";
import FormErrorBanner from "@/components/ui/FormErrorBanner";
export interface PortfolioProps {
  portfolioName: string;
}

export default function Portfolio({ portfolioName }: PortfolioProps) {
  const [displayAssetForm, setDisplayAssetForm] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [assets, setAssets] = useState<AssetRowData[]>([]);
  const { request } = useAuth();

  // Find which assets are associated to this portfolio
  useEffect(() => {
    async function callGetAssets() {
      const assetRows = await getAssets(portfolioName, {
        requestFn: request,
        errorFn: setError,
      });

      setAssets(assetRows);
    }

    callGetAssets();
  }, []);

  const formProps: FormProps = {
    formTitle: "Add new asset",
    cancelForm: () => setDisplayAssetForm(false),
    submitValue: "Add asset",
  };

  return (
    <section className="min-h-0 w-full flex flex-col shadow-sm rounded-3xl p-4">
      <header>
        <h2 className="font-bold mb-1.5">{portfolioName}</h2>
      </header>

      <FormErrorBanner message={error} />
      <article>
        <AssetContainer
          portfolioName={portfolioName}
          stateFn={setAssets}
          assets={assets}
        />
        <Button
          type="button"
          onClick={() => setDisplayAssetForm(true)}
          value="Add new asset"
          variant="blue"
        />
        {displayAssetForm && (
          <AssetForm
            formProps={formProps}
            assetMethod="CREATE"
            portfolioName={portfolioName}
            stateFn={setAssets}
            setError={setError}
          />
        )}
      </article>
    </section>
  );
}
