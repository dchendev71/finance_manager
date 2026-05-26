import { useAuth } from "@/components/auth/AuthContext";
import { z } from "zod";

const AssetRowFormSchema = z.object({
  assetName: z.string().min(1),
  quantity: z.coerce.number(),
  unitPrice: z.coerce.number().gt(0),
});

export default function AssetRowForm({
  portfolioName,
  updateError,
  deactivateForm,
  updateAssetRowList,
}: {
  portfolioName: string;
  updateError: (message: string) => void;
  deactivateForm: () => void;
  updateAssetRowList: (assetRow: any) => void;
}) {
  const { request } = useAuth();
  async function handleForm(formData: FormData): Promise<void> {
    try {
      const values = AssetRowFormSchema.safeParse(Object.fromEntries(formData));
      if (!values.success) {
        console.log(values.error.message);
        updateError(values.error.message);
        return;
      }
      const response = await request("/portfolio/portfolio-asset/create", {
        method: "POST",
        body: JSON.stringify({
          ...values.data,
          portfolioName: portfolioName,
        }),
      });
      // If we reach this scope everything is good
      updateAssetRowList(response);
    } catch (e: any) {
      updateError(e.message);
      return;
    }
  }
  return (
    <>
      <form action={handleForm}>
        <p>
          <label htmlFor="assetName">Asset Name: </label>
          <input type="text" id="assetName" name="assetName" required />
        </p>

        <p>
          <label htmlFor="quantity">Quantity: </label>
          <input type="number" id="quantity" name="quantity" required />
        </p>

        <p>
          <label htmlFor="unitPrice">Unit Price: </label>
          <input type="number" id="unitPrice" name="unitPrice" required />
        </p>

        <p>
          <input type="submit" value="Create new asset" />
        </p>
      </form>

      <button onClick={() => deactivateForm()}>Cancel</button>
    </>
  );
}
