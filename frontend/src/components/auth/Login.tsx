import { Link } from "react-router-dom";
import { useAuth } from "./AuthContext";
import { useNavigate } from "react-router-dom";

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
    <div className="centered-page-container">
      <div className="auth-stack-wrapper">
        <h1
          className="title-primary auth-tile-center"
          style={{ position: "relative", left: "10px" }}
        >
          Login
        </h1>
        <div className="form-container">
          <form className="elegant-form" action={handleAction}>
            <div className="form-group">
              <label htmlFor="email">Email: </label>
              <input
                type="email"
                id="email"
                name="email"
                placeholder="your_email@gmail.com"
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="password">Password: </label>
              <input type="password" id="password" name="password" required />
            </div>
            <div className="form-group">
              <button type="submit" className="btn-primary">
                Login
              </button>
            </div>
            <Link to="/register">Sign up here</Link>
          </form>
        </div>
      </div>
    </div>
  );
}

export default function Login() {
  return <LoginForm />;
}
