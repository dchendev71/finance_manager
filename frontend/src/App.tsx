import { Routes, Route } from "react-router-dom";

import Login from "./components/auth/Login.tsx";
import Register from "./components/auth/Register.tsx";
import Home from "./components/home/Home.tsx";
import Layout from "./components/ui/Layout.tsx";
import UserPage from "./components/user/UserPage.tsx";

export default function App() {
  return (
    <Routes>
      {/* Public Auth Page, No navbar */}
      <Route path="/" element={<Login />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      {/* Auth Page, navbar */}
      <Route element={<Layout />}>
        <Route path="/home" element={<Home />} />
        <Route path="/profile" element={<UserPage />} />
      </Route>
    </Routes>
  );
}
