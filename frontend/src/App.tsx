import { Routes, Route } from "react-router-dom";

import Login from "./components/auth/Login.tsx";
import Register from "./components/auth/Register.tsx";
import Home from "./components/home/Home.tsx";

export default function App() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/home" element={<Home />} />
    </Routes>
  );
}
