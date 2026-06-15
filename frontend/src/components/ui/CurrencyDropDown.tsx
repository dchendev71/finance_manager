import { useEffect, useState } from "react";
import { type Currency } from "../user/UserContext";
import { useAuth } from "../auth/AuthContext";

export default function CurrencyDropDown() {
  const [currencies, setCurrencies] = useState<Currency[]>([]);
  const { request } = useAuth();
  useEffect(() => {
    async function fetchCurrencies() {
      const response: any[] = await request("/currencies");
      setCurrencies(
        response.map(
          (currencyResponse: any) =>
            ({
              symbol: currencyResponse.symbol,
              code: currencyResponse.code,
              name: currencyResponse.name,
            }) as Currency,
        ),
      );
    }

    fetchCurrencies();
  }, []);

  const mostUsed = ["EUR", "USD", "GBP", "CAD", "AUD"];

  const sorted = [
    ...currencies.filter((currency) => mostUsed.includes(currency.code)),
    ...currencies.filter((currency) => !mostUsed.includes(currency.code)),
  ];

  return (
    <>
      <section>
        <div className="relative w-full">
          <select
            id="currencyCode"
            name="currencyCode"
            required
            className="w-full h-12 sm:h-10 pl-3 pr-10 py-2 bg-white border border-slate-300 rounded-lg text-base sm:text-sm appearance-none focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all shadow-sm cursor-pointer text-slate-900 font-medium"
          >
            {sorted.map((currency: Currency) => (
              <option value={currency.code}>
                {currency.code} ({currency.symbol})
              </option>
            ))}
          </select>

          <div className="absolute inset-y-0 right-0 flex items-center pr-3 pointer-events-none text-slate-500">
            <svg
              className="h-5 w-5"
              viewBox="0 0 20 20"
              fill="currentColor"
              aria-hidden="true"
            >
              <path
                fillRule="evenodd"
                d="M5.23 7.21a.75.75 0 011.06.02L10 11.168l3.71-3.938a.75.75 0 111.08 1.04l-4.25 4.5a.75.75 0 01-1.08 0l-4.25-4.5a.75.75 0 01.02-1.06z"
                clipRule="evenodd"
              />
            </svg>
          </div>
        </div>
      </section>
    </>
  );
}
