import { createContext, useContext } from "react";
import type { AssetSubscription } from "./AssetPriceProvider";

export type AssetPriceContextType = {
  subscribe: (symbols: string[]) => void;
  unsubscribe: (symbols: string[]) => void;
  subscriptions: Map<string, AssetSubscription>;
};

export const AssetPriceContext = createContext<AssetPriceContextType | null>(
  null,
);

export const useAssetPrice = (): AssetPriceContextType => {
  const context = useContext(AssetPriceContext);
  if (!context) {
    throw new Error("useAssetPrice must be used within an AssetPriceProvider");
  }
  return context;
};
