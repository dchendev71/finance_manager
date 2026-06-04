import { useEffect, useState } from "react";
import { useAuth } from "@/components/auth/AuthContext";
import { createPortfolio, getPortfolios } from "./api.ts";
import InputField from "@/components/ui/InputField";
import Button from "@/components/ui/Button";
import PortfolioContainer from "./PortfolioContainer";

export default function PortfolioDashboard() {
  const { request } = useAuth();
  const [error, setError] = useState<string | null>(null);
  const [portfolios, setPortfolios] = useState([]);

  useEffect(() => {
    async function handleGetPortfolios() {
      try {
        const response = await getPortfolios({
          requestFn: request,
          errorFn: setError,
        });
        if (response) {
          setPortfolios(response);
        }
      } catch (e: any) {
        setError(e.message);
      }
    }

    handleGetPortfolios();
  }, []);

  async function handleCreatePortfolio(formData: FormData): Promise<void> {
    try {
      const portfolio = await createPortfolio(formData, {
        requestFn: request,
        errorFn: setError,
      });
      if (portfolio) {
        setPortfolios((prev) => [...prev, portfolio]);
      }
    } catch (e: any) {
      setError(e.message || "Network error — please try again");
    }
  }
  return (
    <>
      <main className="min-h-screen w-full flex flex-col bg-slate-50 p-4">
        <header>
          <h1 className="font-bold text-3xl p-4">Portfolio Dashboard</h1>
        </header>

        <article className="min-h-screen w-full flex flex-col border border-slate-100 rounded-3xl bg-white p-8">
          <PortfolioContainer portfolios={portfolios} />
          <form className="flex flex-col" action={handleCreatePortfolio}>
            <InputField
              label="Portfolio's Name"
              id="portfolioName"
              type="text"
            />
            <Button value="Create new portfolio" variant="blue" />
          </form>
        </article>
      </main>
    </>
  );

  // return (
  //   <>
  //     <main className="dashboard-container">
  //       <header className="main-header">
  //         <h1 className="title-primary">Portfolio Dashboard</h1>
  //       </header>
  //
  //       {portfolioList.length > 0 && (
  //         <section className="list-level-1">
  //           <article className="card-level-1">
  //             {portfolioList.map((portfolio: any) => (
  //               <Portfolio
  //                 key={portfolio.portfolioName}
  //                 portfolioName={portfolio.portfolioName}
  //               />
  //             ))}
  //           </article>
  //         </section>
  //       )}
  //       <div className="form-container">
  //         <form className="elegant-form" action={handleCreatePortfolio}>
  //           <div className="form-group">
  //             <label htmlFor="portfolioName">Portfolio's Name</label>
  //             <input
  //               type="text"
  //               id="portfolioName"
  //               name="portfolioName"
  //               required
  //             />
  //           </div>
  //           <button type="submit" className="btn-primary">
  //             Create Portfolio
  //           </button>
  //         </form>
  //       </div>
  //     </main>
  //   </>
  // );
}
