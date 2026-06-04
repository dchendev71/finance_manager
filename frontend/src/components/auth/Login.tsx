import { Link } from "react-router-dom";
import { useAuth } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import InputField from "@/components/ui/InputField";
import FormErrorBanner from "@/components/ui/FormErrorBanner";
import { useState } from "react";
import Button from "@/components/ui/Button";

function LoginForm() {
  const [error, setError] = useState<string | null>(null);

  const { login, request } = useAuth();
  const navigate = useNavigate();

  async function handleAction(formData: FormData): Promise<void> {
    try {
      const email = formData.get("email") as String | null;
      const password = formData.get("password") as String | null;
      if (!email || !password) {
        setError("Email and password are required fields.");
        return;
      }
      const res = await request("/auth/login", {
        method: "POST",
        body: JSON.stringify({
          email: email,
          password: password,
        }),
      });

      if (res != null) {
        login(res.jwtToken);
        navigate("/home");
      }
    } catch (e: any) {
      setError(e.message || "Network error — please try again");
    }
  }

  return (
    <>
      <main className="min-h-screen w-full flex flex-col items-center justify-center bg-slate-50 p-4">
        <article className="min-h-screen w-full sm:min-h-0 sm:max-w-md bg-white p-6 sm:p-8 sm:rounded-3xl sm:shadow-md sm:border sm:border-slate-100 flex flex-col justify-center">
          <header className="text-center mb-8 sm:mb-6">
            <h1 className="font-bold">Login</h1>
          </header>
          <FormErrorBanner message={error} />
          <form className="flex flex-col gap-5 sm:gap-4" action={handleAction}>
            <InputField id="email" type="email" label="Email" />
            <InputField id="password" type="password" label="Password" />
            <Button value="Login" variant="blue" />
            <Link to="/register">Sign up here</Link>
          </form>
        </article>
      </main>
    </>
  );
}

export default function Login() {
  return <LoginForm />;
}
