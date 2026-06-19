import { useAuth } from "@/components/auth/AuthContext";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import {
  AssetPriceContext,
  type AssetPriceContextType,
} from "./AssetPriceContext";

interface AssetPriceProviderProps {
  children: React.ReactNode;
}

export type AssetSubscription = {
  watcherCount: number;
  price: number | null;
};

export function AssetPriceProvider({ children }: AssetPriceProviderProps) {
  const ws = useRef<WebSocket | null>(null);
  const WS_URL = "ws://localhost:8080/ws/prices";
  const [subscriptions, setSubscriptions] = useState<
    Map<string, AssetSubscription>
  >(new Map());

  const subscribe = useCallback((symbols: string[]) => {
    setSubscriptions((prev) => {
      const next = new Map(prev);
      const newSymbols: string[] = [];

      symbols.forEach((symbol) => {
        const current = next.get(symbol);
        if (!current) {
          newSymbols.push(symbol);
          next.set(symbol, { watcherCount: 1, price: null });
        } else {
          next.set(symbol, {
            ...current,
            watcherCount: current.watcherCount + 1,
          });
        }
      });

      if (newSymbols.length > 0) {
        ws.current?.send(
          JSON.stringify({ action: "subscribe", symbols: newSymbols }),
        );
      }

      return next;
    });
  }, []);

  const unsubscribe = useCallback((symbols: string[]) => {
    setSubscriptions((prev) => {
      const next = new Map(prev);
      const toUnsubscribe: string[] = [];

      symbols.forEach((symbol) => {
        const current = next.get(symbol);
        if (!current) return;
        if (current.watcherCount <= 1) {
          toUnsubscribe.push(symbol);
          next.delete(symbol);
        } else {
          next.set(symbol, {
            ...current,
            watcherCount: current.watcherCount - 1,
          });
        }
      });

      if (toUnsubscribe.length > 0) {
        ws.current?.send(
          JSON.stringify({ action: "unsubscribe", symbols: toUnsubscribe }),
        );
      }

      return next;
    });
  }, []);

  useEffect(() => {
    const { token } = useAuth();
    ws.current = new WebSocket(`${WS_URL}?token=${token}`);

    ws.current.onopen = () => {
      console.log("WebSocket connected");
    };

    ws.current.onmessage = (event) => {
      const { symbol, price } = JSON.parse(event.data);
      setSubscriptions((prev) => {
        const next = new Map(prev);
        const current = next.get(symbol);
        if (current) {
          next.set(symbol, { ...current, price });
        }
        return next;
      });
    };

    ws.current.onerror = (error) => {
      console.error("WebSocket error:", error);
    };

    ws.current.onclose = () => {
      console.log("WebSocket closed");
    };

    return () => {
      ws.current?.close();
    };
  }, []);

  const contextValue: AssetPriceContextType = useMemo(
    () => ({
      subscribe,
      unsubscribe,
      subscriptions,
    }),
    [subscriptions],
  );

  return (
    <AssetPriceContext.Provider value={contextValue}>
      {children}
    </AssetPriceContext.Provider>
  );
}
