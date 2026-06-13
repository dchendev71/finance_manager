import { createContext, useContext } from "react";

export type BalanceContextType = {
  balance: number | null;
  error: string | null;
  refreshBalance: () => void;
};

export const BalanceContext = createContext<BalanceContextType | null>(null);

export const useBalance = (): BalanceContextType => {
  const context = useContext(BalanceContext);
  if (!context) {
    throw new Error("useBlance must be used within an BalanceProvider");
  }
  return context;
};
