import Button from "@/components/ui/Button";
import { changeCurrency } from "@/components/user/api";
import { useAuth } from "@/components/auth/AuthContext";
import { useUser } from "@/components/user/UserContext";
import { useState } from "react";
import CurrencyDropDown from "@/components/ui/CurrencyDropDown";

export default function ChangeCurrencyForm() {
  const { request } = useAuth();
  const { setUser } = useUser();
  const [error, setError] = useState<string | null>(null);
  async function handleAction(formData: FormData) {
    // TODO: Check if OK
    await changeCurrency(
      formData,
      {
        requestFn: request,
        errorFn: setError,
      },
      setUser,
    );
  }
  return (
    <>
      <form className="flex flex-col gap-5" action={handleAction}>
        <CurrencyDropDown />
        <Button value="Change Currency" variant="blue" type="submit" />
      </form>
    </>
  );
}
