import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "./AuthContext";
import InputField from "../ui/InputField";
import FormErrorBanner from "@/components/ui/FormErrorBanner";
import BlueButton from "../ui/BlueButton";

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
      navigate("/login");
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
            <section>
              <div className="relative w-full">
                <select
                  id="currencyCode"
                  name="currencyCode"
                  required
                  className="w-full h-12 sm:h-10 pl-3 pr-10 py-2 bg-white border border-slate-300 rounded-lg text-base sm:text-sm appearance-none focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all shadow-sm cursor-pointer text-slate-900 font-medium"
                >
                  <option value="EUR">EUR (€)</option>
                  <option value="USD">USD ($)</option>
                  <option value="GBP">GBP (£)</option>
                  <option value="CAD">CAD ($)</option>
                  <option value="AUD">AUD ($)</option>
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
            <BlueButton value="Register" />
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
