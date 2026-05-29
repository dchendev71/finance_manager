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
      <main className="dashboard-container">
        <header className="main-header">
          <h1 className="title-primary">Portfolio Dashboard</h1>
        </header>

        {portfolioList.length > 0 && (
          <section className="list-level-1">
            <article className="card-level-1">
              {portfolioList.map((portfolio: any) => (
                <Portfolio
                  key={portfolio.portfolioName}
                  portfolioName={portfolio.portfolioName}
                />
              ))}
            </article>
          </section>
        )}
        <div className="form-container">
          <form className="elegant-form" action={handleCreatePortfolio}>
            <div className="form-group">
              <label htmlFor="portfolioName">Portfolio's Name</label>
              <input
                type="text"
                id="portfolioName"
                name="portfolioName"
                required
              />
            </div>
            <button type="submit" className="btn-primary">
              Create Portfolio
            </button>
          </form>
        </div>
      </main>
    </>
  );
}
