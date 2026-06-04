import type { CallerFunction } from "@/types/global";
import { z } from "zod";

type AssetData = {
  assetName: string;
  tickerSymbol: string;
  assetType: string;
};

export type AssetRowData = {
  asset: AssetData;
  quantity: number;
  meanPrice: number;
};

function assetRowMapperToAssetData(assetRow: any): AssetRowData {
  return {
    quantity: assetRow.quantity,
    meanPrice: assetRow.meanPrice,
    asset: {
      assetName: assetRow.assetResponse.name,
      tickerSymbol: assetRow.assetResponse.tickerSymbol,
      assetType: assetRow.assetResponse.assetTypeResponse.type,
    } as AssetData,
  } as AssetRowData;
}

export async function getAssets(
  portfolioName: string,
  callerFn: CallerFunction,
): Promise<AssetRowData[]> {
  try {
    const response = await callerFn.requestFn(`/portfolios/${portfolioName}`);

    const assetRows: AssetRowData[] = response.map((assetRow: any) =>
      assetRowMapperToAssetData(assetRow),
    );

    return assetRows;
  } catch (e: any) {
    callerFn.errorFn(e.message || "Network error - please try again");
    return [];
  }
}

const AssetFormSchema = z.object({
  assetName: z.string().min(1),
  quantity: z.coerce.number(),
  unitPrice: z.coerce.number().gt(0),
});

export async function handleAssetForm(
  formData: FormData,
  method: "CREATE" | "BUY" | "SELL",
  portfolioName: string,
  callerFn: CallerFunction,
): Promise<any> {
  try {
    const values = AssetFormSchema.safeParse(Object.fromEntries(formData));
    if (!values.success) {
      callerFn.errorFn(values.error.message);
      return;
    }
    const response = await callerFn.requestFn(`/portfolios/${portfolioName}`, {
      method: method == "CREATE" ? "POST" : "PATCH",
      body: JSON.stringify({
        ...values.data,
      }),
    });

    // TODO: check for null for no content
    console.log(response);
  } catch (e: any) {
    callerFn.errorFn(e.message || "Network error - please try again");
    return;
  }
}
