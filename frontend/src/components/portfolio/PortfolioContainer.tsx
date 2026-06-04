import Portfolio, { type PortfolioProps } from "./Portfolio";

interface PortfolioContainerProps {
  portfolios: PortfolioProps[];
}
export default function PortfolioContainer({
  portfolios,
}: PortfolioContainerProps) {
  return (
    <>
      {portfolios.length > 0 &&
        portfolios.map((portfolio: PortfolioProps) => (
          <Portfolio
            key={portfolio.portfolioName}
            portfolioName={portfolio.portfolioName}
          />
        ))}
    </>
  );
}
