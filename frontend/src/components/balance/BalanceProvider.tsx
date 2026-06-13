import { useCallback, useEffect, useMemo, useState } from "react";
import { BalanceContext, type BalanceContextType } from "./BalanceContext";
import { useAuth } from "../auth/AuthContext";
import { fetchBalance } from "./api";

interface BalanceProviderProps {
  children: React.ReactNode;
}

export function BalanceProvider({ children }: BalanceProviderProps) {
  const [balance, setBalance] = useState<number | null>(null);
  const [error, setError] = useState<string | null>(null);
  const { request } = useAuth();

  const refreshBalance = useCallback(async () => {
    const newBalance = await fetchBalance({
      requestFn: request,
      errorFn: setError,
    });

    setBalance(newBalance.balance);
  }, [request]);

  useEffect(() => {
    // Fetch balance on mount
    refreshBalance();
  }, [refreshBalance]);

  const contextValue: BalanceContextType = useMemo(
    () => ({
      balance,
      error,
      refreshBalance,
    }),
    [balance, error, refreshBalance],
  );

  return (
    <BalanceContext.Provider value={contextValue}>
      {children}
    </BalanceContext.Provider>
  );
}
