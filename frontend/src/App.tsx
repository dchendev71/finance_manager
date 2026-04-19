import { Routes, Route } from "react-router-dom"

import AuthRoutes from "./components/auth/authRoutes"

export default function App() { 
    return (
        <Routes>
            { AuthRoutes }
        </Routes>
    )
}