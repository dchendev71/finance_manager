import { useState } from "react"
import { Link } from "react-router-dom"
import authStyles from "./auth.module.css"

function RegisterForm() {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")

    const handleSubmit = async (e: React.SyntheticEvent) => {
        e.preventDefault()

        // TODO: Change API URL
        const res = await fetch("https://localhost:4000", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        })
    }

    return (
        <div className={authStyles.formContainer}>
            <form onSubmit={handleSubmit}>
                <p>
                    <label htmlFor="email">Email: </label>
                    <input type="email" id="email" name="email" value={email}
                        onChange={(e) => setEmail(e.target.value)}>
                    </input>
                </p>
                <p>
                    <label htmlFor="password">Password: </label>
                    <input type="password" id="password" name="password" value={password}
                        onChange={(e) => setPassword(e.target.value)}>
                    </input>
                </p>
                <p>
                    <input type="submit" value="Sign up"></input>
                </p>

                <Link to="/Login">Already an account?</Link>
            </form>
        </div>
    )
}

export default function Register() {
    return (
        <RegisterForm />
    )
}