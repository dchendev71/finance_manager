import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import authStyles from "./auth.module.css";

function RegisterForm() {
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  async function handleAction(formData) {
    setError("");
    setLoading(true);

    try {
      const res = await fetch("http://localhost:8080/api/v1/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email: formData.get("email"),
          password: formData.get("password"),
          currencyCode: "EUR",
          // currencyCode: formData.get("currencyCode"),
        }),
      });

      if (!res.ok) {
        const errorData = await res.json();
        setError(errorData.message || "Registration failed");
        return;
      }
      // redirect to login
      navigate("/login");
    } catch (err) {
      setError("Network error — please try again");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className={authStyles.formContainer}>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <form action={handleAction()}>
        <p>
          <label htmlFor="email">Email: </label>
          <input type="email" id="email" name="email" required />
        </p>
        <p>
          <label htmlFor="password">Password: </label>
          <input type="password" id="password" name="password" required />
        </p>
        <p>
          <label htmlFor="currencyCode">Currency: </label>
          <select id="currencyCode" name="currencyCode" required>
            <option value="">Select currency</option>
            <option value="USD">USD - US Dollar</option>
            <option value="EUR">EUR - Euro</option>
            <option value="GBP">GBP - British Pound</option>
            <option value="CAD">CAD - Canadian Dollar</option>
            <option value="AUD">AUD - Australian Dollar</option>
          </select>
        </p>
        <p>
          <input
            type="submit"
            value={loading ? "Signing up..." : "Sign up"}
            disabled={loading}
          />
        </p>
        <Link to="/login">Already have an account?</Link>
      </form>
    </div>
  );
}

export default function Register() {
  return <RegisterForm />;
}
