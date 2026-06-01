import { Link } from "react-router-dom";
import { useAuth } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import InputField from "@/components/ui/InputField";

function LoginForm() {
  const { login, request } = useAuth();
  const navigate = useNavigate();

  async function handleAction(formData: FormData): Promise<void> {
    try {
      const email = formData.get("email") as String | null;
      const password = formData.get("password") as String | null;
      if (!email || !password) {
        throw new Error("Email and password are required fields.");
      }
      const res = await request("/auth/login", {
        method: "POST",
        body: JSON.stringify({
          email: formData.get("email"),
          password: formData.get("password"),
        }),
      });

      if (res != null) {
        login(res.jwtToken);
        navigate("/home");
        // TODO: redirect to another page
      }
    } catch (e) {
      // TODO: Do something smart
    }
  }

  return (
    <>
      <main className="min-h-screen w-full flex flex-col items-center justify-center bg-slate-50 p-4">
        <article className="min-h-screen w-full sm:min-h-0 sm:max-w-md bg-white p-6 sm:p-8 sm:rounded-3xl sm:shadow-md sm:border sm:border-slate-100 flex flex-col justify-center">
          <header className="text-center mb-8 sm:mb-6">
            <h1 className="font-bold">Login</h1>
          </header>
          <form className="flex flex-col gap-5 sm:gap-4" action={handleAction}>
            <InputField id="email" type="email" label="Email: " />
            <InputField id="password" type="password" label="Password: " />
            <button
              type="submit"
              className="w-full h-12 sm:h-10 bg-blue-600 hover:bg-blue-700 active:bg-blue-800 text-white font-medium rounded-lg transition-colors mt-2 text-base sm:text-sm shadow-sm"
            >
              Login
            </button>
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
