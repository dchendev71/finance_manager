import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "./AuthContext";
import InputField from "../ui/InputField";
import FormErrorBanner from "@/components/ui/FormErrorBanner";
import Button from "../ui/Button";
import CurrencyDropDown from "../ui/CurrencyDropDown";

function RegisterForm() {
  const [error, setError] = useState<string | null>(null);
  const { request } = useAuth();
  const navigate = useNavigate();

  async function handleAction(formData: FormData): Promise<void> {
    setError(null);

    const email = formData.get("email") as string | null;
    const password = formData.get("password") as string | null;
    const currencyCode = formData.get("currencyCode") as string | "EUR";

    try {
      await request("/auth/register", {
        method: "POST",
        body: JSON.stringify({
          email: email,
          password: password,
          currencyCode: currencyCode,
        }),
      });

      // If we reach this step, login is succesful
      // TODO: Maybe create a pop up Registration Succesful?
      // redirect to login
      navigate("/login", {
        state: {
          successMessage: "Registration complete!",
        },
      });
    } catch (err: any) {
      setError(err.message || "Network error — please try again");
    }
  }

  return (
    <>
      <main className="min-h-screen w-full flex flex-col items-center justify-center bg-slate-50 p-4">
        <article className="min-h-screen w-full sm:min-h-0 sm:max-w-md bg-white p-6 sm:p-8 sm:rounded-3xl sm:shadow-md sm:border sm:border-slate-100 flex flex-col justify-center">
          <header className="text-center mb-8 sm:mb-6">
            <h1 className="font-bold">Register</h1>
          </header>

          <FormErrorBanner message={error} />
          <form className="flex flex-col gap-5 sm:gap-4" action={handleAction}>
            <InputField id="email" type="email" label="Email" />
            <InputField id="password" type="password" label="Password" />
            <CurrencyDropDown />
            <Button value="Register" variant="blue" />
            <Link to="/login">Already have an account?</Link>
          </form>
        </article>
      </main>
    </>
  );
}

export default function Register() {
  return <RegisterForm />;
}
