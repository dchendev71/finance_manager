import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "./AuthContext";

function RegisterForm() {
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const { request } = useAuth();
  const navigate = useNavigate();

  async function handleAction(formData: FormData): Promise<void> {
    setError("");
    setLoading(true);

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
    } finally {
      setLoading(false);
    }
  }

  return (
    // {error && <p style={{ color: "red" }}>{error}</p>}
    <div className="centered-page-container">
      <div className="auth-stack-wrapper">
        <h1
          className="title-primary auth-tile-center"
          style={{ position: "relative", left: "10px" }}
        >
          Register
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
              <label htmlFor="currencyCode">Currency: </label>
              <select id="currencyCode" name="currencyCode" required>
                <option value="EUR">EUR</option>
                <option value="USD">USD</option>
                <option value="GBP">GBP</option>
                <option value="CAD">CAD</option>
                <option value="AUD">AUD</option>
              </select>
            </div>
            <button type="submit" className="btn-primary">
              Sign up
            </button>
            <Link to="/login">Already have an account?</Link>
          </form>
        </div>
      </div>
    </div>
  );
}

export default function Register() {
  return <RegisterForm />;
}
