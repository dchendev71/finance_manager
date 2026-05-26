import { useState } from "react";
import AssetRowForm from "./asset_row/AssetRowForm";

export interface PortfolioProps {
  portfolioName: string;
}

export default function Portfolio({ portfolioName }: PortfolioProps) {
  const [showForm, setShowForm] = useState<boolean>(false);
  const [error, setError] = useState<string>("");
  const [assetRowList, setAssetRowList] = useState([]);

  async function handleClick() {
    setShowForm((prev) => !prev);
  }

  return (
    <>
      {showForm ? (
        <AssetRowForm
          portfolioName={portfolioName}
          updateError={(err: string) => setError(err)}
          deactivateForm={() => setShowForm(false)}
          updateAssetRowList={(assetRow: any) =>
            setAssetRowList((prev) => [...prev, assetRow])
          }
        />
      ) : (
        <>
          <h1>{portfolioName}</h1>
          <button onClick={handleClick}>Add asset</button>
        </>
      )}
    </>
  );
}
