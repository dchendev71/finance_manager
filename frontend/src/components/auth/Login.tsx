import { Link } from "react-router-dom";
import authStyles from "./auth.module.css";

function LoginForm() {
  // TODO: Change API URL
  async function handleAction(formData) {
    const res = await fetch("http://localhost:8080/api/v1/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email: formData.get("email"),
        password: formData.get("password"),
      }),
    });
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
