import { Route } from "react-router-dom"
import Login from "./Login"
import Register from "./Register"

export default function AuthRoutes() {
    return (
        <>
            <Route path="/" element={<Login/> } />
            <Route path="/register" element={<Register /> } />
        </>
    )
}