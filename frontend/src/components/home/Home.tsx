import { useAuth } from "@/components/auth/AuthContext";
import { useEffect, useState } from "react";
import {
  createPortfolio,
  getPortfolios,
} from "@/components/portfolio/portfolioApi";

export default function Home() {
  const { request } = useAuth();
  const [error, setError] = useState<string>("");
  const [portfolioList, setPortfolioList] = useState([]);

  useEffect(() => {
    async function handleFetch() {
      try {
        const response = await getPortfolios(request);
        if (response) {
          setPortfolioList(response);
        }
      } catch (e: any) {
        setError(e.message);
      }
    }

    handleFetch();
  }, []);
  async function createPortfolioCaller(formData: FormData): Promise<void> {
    try {
      const newPortfolio = await createPortfolio(formData, request);
      if (newPortfolio) {
        setPortfolioList((prev) => [...prev, newPortfolio]);
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
            <li key={portfolio.portfolioName}>
              <strong>{portfolio.portfolioName}</strong>
            </li>
          ))}
        </ul>
      )}
      <form action={createPortfolioCaller}>
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
