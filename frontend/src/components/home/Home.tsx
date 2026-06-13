import PortfolioDashboard from "@/components/portfolio/PortfolioDashboard";
import { BalanceProvider } from "@/components/balance/BalanceProvider";

export default function Home() {
  return (
    <>
      <BalanceProvider>
        <PortfolioDashboard />
      </BalanceProvider>
    </>
  );
}
