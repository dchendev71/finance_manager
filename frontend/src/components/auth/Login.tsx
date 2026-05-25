import { Link } from "react-router-dom";
import authStyles from "./auth.module.css";
import { useAuth } from "./AuthContext";

function LoginForm() {
  const { login, request } = useAuth();
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
        // TODO: redirect to another page
      }
    } catch (e) {
      // TODO: Do something smart
    }
  }

  return (
    <div className={authStyles.formContainer}>
      <form action={handleAction}>
        <p>
          <label htmlFor="email">Email: </label>
          <input type="text" id="email" name="email" required />
        </p>
        <p>
          <label htmlFor="password">Password: </label>
          <input type="password" id="password" name="password" required />
        </p>
        <p>
          <input type="submit" value="login"></input>
        </p>
        <Link to="/register">Sign up here</Link>
      </form>
    </div>
  );
}

export default function Login() {
  return <LoginForm />;
}
