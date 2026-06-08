import type { CallerFunction } from "@/types/global";
import type { Dispatch, SetStateAction } from "react";
import { z } from "zod";
import type { AssetMethod } from "./AssetForm";

export type AssetData = {
  assetName: string;
  tickerSymbol: string;
  assetType: string;
};

export type AssetRowData = {
  asset: AssetData;
  quantity: number;
  meanPrice: number;
};

function responseToAssetRowData(assetRow: any): AssetRowData {
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
      responseToAssetRowData(assetRow),
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

function handleAssetChange(
  method: AssetMethod,
  stateFn: Dispatch<SetStateAction<AssetRowData[]>>,
  assetName: string,
  assetRow?: AssetRowData,
): void {
  if (method === "CREATE") {
    stateFn((prev) => [...prev, assetRow]);
    return;
  }
  // If we have an assetRow, it means we need to update the state
  // And the method is either SELL or BUY
  if (assetRow) {
    stateFn((prev) =>
      prev.map((p) => (p.asset.assetName == assetName ? assetRow : p)),
    );
    return;
  }
  // This case is when we sold everything on the current asset
  // Note: It should only happen when method is SELL
  if (method === "SELL") {
    stateFn((prev) =>
      prev.filter((assetRow) => assetRow.asset.assetName !== assetName),
    );
  }
}

export async function handleAssetForm(
  formData: FormData,
  method: AssetMethod,
  portfolioName: string,
  stateFn: Dispatch<SetStateAction<AssetRowData[]>>,
  callerFn: CallerFunction,
): Promise<any> {
  try {
    const values = AssetFormSchema.safeParse(Object.fromEntries(formData));
    if (!values.success) {
      callerFn.errorFn(values.error.message);
      return;
    }

    // Enforce that Quantity is negative if 'Sell'
    const quantity =
      method === "SELL"
        ? -Math.abs(values.data.quantity)
        : Math.abs(values.data.quantity);

    const response = await callerFn.requestFn(`/portfolios/${portfolioName}`, {
      method: method == "CREATE" ? "POST" : "PATCH",
      body: JSON.stringify({
        ...values.data,
        quantity: quantity,
      }),
    });

    if (response) {
      handleAssetChange(
        method,
        stateFn,
        values.data.assetName,
        responseToAssetRowData(response),
      );
    } else {
      // This mean that we sold everything in the Asset when we updated
      handleAssetChange(method, stateFn, values.data.assetName);
    }

    // TODO: check for null for no content
  } catch (e: any) {
    callerFn.errorFn(e.message || "Network error - please try again");
    return;
  }
}
