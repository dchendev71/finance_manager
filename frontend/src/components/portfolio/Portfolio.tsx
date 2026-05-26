import { useAuth } from "@/components/auth/AuthContext";

interface AssetRowRequest {
  portfolioName: string;
  assetName: string;
  quantity: number;
  unitPrice: number;
}

export default function Portfolio(props) {
  const { request } = useAuth();
  const portfolioName: string = props.portfolioName;

  async function createAssetRow(formData: FormData): Promise<void> {
    const apiPayload: AssetRowRequest = {
      portfolioName: portfolioName,
      assetName: formData.get("assetName") as string,
      quantity: formData.get("quantity") as number,
      unitPrice: formData.get("unitPrice") as number,
    };
    await request("/portfolio/portfolio-asset/create", {
      method: "POST",
      body: JSON.stringify(apiPayload),
    });
  }
  // Create a form that allow one to create an asset row
  return (
    <form action={createAssetRow}>
      <p>
        <label htmlFor="assetName">Asset Name: </label>
        <input type="text" id="assetName" name="assetName" required />
      </p>
      <p>
        <label htmlFor="quantity">quantity: </label>
        <input type="number" id="quantity" name="quantity" required />
      </p>
      <p>
        <label htmlFor="unitPrice">unitPrice: </label>
        <input type="number" id="unitPrice" name="unitPrice" required />
      </p>
    </form>
  );
}
