import { useEffect, useState } from "react";
import { useAuth } from "@/components/auth/AuthContext";
import { createPortfolio, getPortfolios } from "./portfolioApi";
import Portfolio from "./Portfolio";

export default function PortfolioDashboard() {
  const { request } = useAuth();
  const [error, setError] = useState<string>("");
  const [portfolioList, setPortfolioList] = useState([]);

  useEffect(() => {
    async function handleGetPortfolios() {
      try {
        const response = await getPortfolios(request);
        if (response) {
          setPortfolioList(response);
        }
      } catch (e: any) {
        setError(e.message);
      }
    }

    handleGetPortfolios();
  }, []);

  async function handleCreatePortfolio(formData: FormData): Promise<void> {
    try {
      const portfolio = await createPortfolio(formData, request);
      if (portfolio) {
        setPortfolioList((prev) => [...prev, portfolio]);
      }
    } catch (e: any) {
      setError(e.message);
    }
  }

  return (
    <>
      <h1>Your Portfolios</h1>
      {portfolioList.length === 0 ? (
        <p>You don't have any portfolios! Create one below!</p>
      ) : (
        <ul>
          {portfolioList.map((portfolio: any) => (
            <Portfolio
              key={portfolio.portfolioName}
              portfolioName={portfolio.portfolioName}
            />
          ))}
        </ul>
      )}
      <form action={handleCreatePortfolio}>
        <p>
          <label htmlFor="portfolioName">Portfolio's Name</label>
          <input type="text" id="portfolioName" name="portfolioName" required />
        </p>
        <p>
          <input type="submit" value="Create portfolio"></input>
        </p>
      </form>
    </>
  );
}
