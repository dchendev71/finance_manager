import Button from "@/components/ui/Button";
import { updateUser, type UpdateUserPayload } from "@/components/user/api";
import { useAuth } from "@/components/auth/AuthContext";
import { useUser } from "@/components/user/UserContext";
import { useState } from "react";
import CurrencyDropDown from "@/components/ui/CurrencyDropDown";
import FormErrorBanner from "@/components/ui/FormErrorBanner";
import FormSuccessBanner from "@/components/ui/FormSuccessBanner";

export default function ChangeCurrencyForm() {
  const { request } = useAuth();
  const { setUser } = useUser();
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  async function handleAction(formData: FormData) {
    const payload: UpdateUserPayload = {
      formData: formData,
      callerFn: {
        requestFn: request,
        errorFn: setError,
      },
      setUser: setUser,
      config: {
        endpoint: "/users/change-currency",
        method: "PATCH",
        requiredFields: ["currencyCode"],
      },
    };

    try {
      await updateUser(payload);
      setSuccessMessage("Currency update succesful!");
    } catch (e: any) {
      setError(e.message || "Network error - please try again");
    }
  }
  return (
    <>
      <FormSuccessBanner message={successMessage} />
      <FormErrorBanner message={error} />
      <form className="flex flex-col gap-5" action={handleAction}>
        <CurrencyDropDown />
        <Button value="Change Currency" variant="blue" type="submit" />
      </form>
    </>
  );
}
