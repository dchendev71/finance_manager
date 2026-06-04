import { useEffect, useState } from "react";
import { useAuth } from "@/components/auth/AuthContext";
import { getAssets, type AssetRowData } from "./asset/api";
import AssetForm from "./asset/AssetForm";
import AssetContainer from "./asset/AssetContainer";
export interface PortfolioProps {
  portfolioName: string;
}

export default function Portfolio({ portfolioName }: PortfolioProps) {
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

  return (
    <section className="min-h-0 w-full flex flex-col shadow-sm rounded-3xl p-4">
      <header>
        <h2 className="font-bold">{portfolioName}</h2>
      </header>
      <article>
        <AssetContainer assets={assets} />
        <AssetForm portfolioName={portfolioName} />
      </article>
    </section>
  );
}

// export default function Portfolio({ portfolioName }: PortfolioProps) {
//   const [showForm, setShowForm] = useState<boolean>(false);
//   const [error, setError] = useState<string>("");
//   const [assetRowList, setAssetRowList] = useState([]);
//   const { request } = useAuth();
//
//   useEffect(() => {
//     async function getAssetRowList() {
//       try {
//         const response = await request(
//           `/portfolio/portfolio-asset/list/${portfolioName}`,
//         );
//         if (response) {
//           // First trim the response to just get the assetRow
//           const cleanedList = response.map(
//             (assetRowResponse: any) => assetRowResponse.assetResponse,
//           );
//           setAssetRowList(cleanedList);
//         }
//       } catch (e: any) {
//         setError(e.message || "Network error - please try again");
//       }
//     }
//
//     getAssetRowList();
//   }, []);
//
//   async function handleClick() {
//     setShowForm((prev) => !prev);
//   }
//
//   return (
//     <>
//       {showForm ? (
//         <AssetRowForm
//           portfolioName={portfolioName}
//           updateError={(err: string) => setError(err)}
//           deactivateForm={() => setShowForm(false)}
//           updateAssetRowList={(assetRow: any) =>
//             setAssetRowList((prev) => [...prev, assetRow.assetResponse])
//           }
//         />
//       ) : (
//         <>
//           <h2 className="title-secondary">{portfolioName}</h2>
//
//           <div className="card-level-2">
//             {assetRowList.length > 0 && (
//               <ul className="list-level-3">
//                 {assetRowList.map((assetRow: any) => (
//                   <AssetRow key={assetRow.name} assetName={assetRow.name} />
//                 ))}
//               </ul>
//             )}
//             <div className="button-group-left">
//               <button className="btn-primary" onClick={handleClick}>
//                 Add asset
//               </button>
//             </div>
//           </div>
//         </>
//       )}
//     </>
//   );
// }
