import { useEffect, useState } from "react";
import AssetRowForm from "./asset_row/AssetRowForm";
import AssetRow from "./asset_row/AssetRow";
import { useAuth } from "@/components/auth/AuthContext";

export interface PortfolioProps {
  portfolioName: string;
}

export default function Portfolio({ portfolioName }: PortfolioProps) {
  const [showForm, setShowForm] = useState<boolean>(false);
  const [error, setError] = useState<string>("");
  const [assetRowList, setAssetRowList] = useState([]);
  const { request } = useAuth();

  useEffect(() => {
    async function getAssetRowList() {
      try {
        const response = await request(
          `/portfolio/portfolio-asset/list/${portfolioName}`,
        );
        if (response) {
          // First trim the response to just get the assetRow
          const cleanedList = response.map(
            (assetRowResponse: any) => assetRowResponse.assetResponse,
          );
          setAssetRowList(cleanedList);
        }
      } catch (e: any) {
        setError(e.message || "Network error - please try again");
      }
    }

    getAssetRowList();
  }, []);

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
          {assetRowList.length > 0 &&
            assetRowList.map((assetRow: any) => (
              <AssetRow key={assetRow.name} assetName={assetRow.name} />
            ))}

          <button onClick={handleClick}>Add asset</button>
        </>
      )}
    </>
  );
}
