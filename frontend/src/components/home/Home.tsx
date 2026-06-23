import PortfolioDashboard from "@/components/portfolio/PortfolioDashboard";
import { BalanceProvider } from "@/components/balance/BalanceProvider";
import { AssetPriceProvider } from "@/components/asset/AssetPriceProvider";

export default function Home() {
  return (
    <>
      <BalanceProvider>
        <AssetPriceProvider>
          <PortfolioDashboard />
        </AssetPriceProvider>
      </BalanceProvider>
    </>
  );
}
