import { Link } from "react-router"
import { useState } from "react"
import authStyles from "./auth.module.css"

// We need a username, a password
function LoginForm() {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")

    // TODO: Change API URL
    const handleSubmit = async (e) => {
        e.preventDefault()
        const res = await fetch("http://localhost:4000/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        })
    }

    return (
        <div className={authStyles.formContainer}>
            <form onSubmit={handleSubmit}>
                <p>
                    <label htmlFor="username">Username: </label>
                    <input type="text" id="username" name="username" value={username}
                        onChange={(e) => setUsername(e.target.value)}>
                    </input>
                </p>
                <p>
                    <label htmlFor="password">Password: </label>
                    <input type="password" id="password" name="password" value={password}
                        onChange={(e) => setPassword(e.target.value)}>
                    </input>
                </p>
                <p>
                    <input type="submit" value="login"></input>
                </p>
            </form>
        </div>
    )
}

export default function Login() {
    return (
        <LoginForm />
    )
}