import { useBalance } from "@/components/balance/BalanceContext";
import { useUser } from "@/components/user/UserContext";
import Button from "./Button";
import { increaseBalance } from "../balance/api";
import { useAuth } from "../auth/AuthContext";
import { useState } from "react";
import FormErrorBanner from "./FormErrorBanner";

export default function UserBalance() {
  const { balance, refreshBalance } = useBalance();
  const { user } = useUser();
  const [error, setError] = useState<string | null>(null);
  const { request } = useAuth();

  // TODO: Maybe create a form for the amount?
  const increaseAmount = 10000;

  async function onClick() {
    await increaseBalance(
      {
        requestFn: request,
        errorFn: setError,
      },
      increaseAmount,
    );

    refreshBalance();
  }

  return (
    <>
      <FormErrorBanner message={error} />
      <section className="ml-auto flex flex-row">
        <h1 className="font-bold text-3xl p-4">
          Balance: {balance}
          {user?.currency.symbol}
        </h1>
        <Button value="Increase" variant="green" onClick={onClick}></Button>
      </section>
    </>
  );
}
