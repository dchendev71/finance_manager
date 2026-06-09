import { Navigate, Outlet } from "react-router-dom";
import Navbar from "./Navbar";
import { useAuth } from "@/components/auth/AuthContext";

export default function ProtectedLayout() {
  const { isAuthenticated } = useAuth();
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  return (
    <>
      <Navbar />
      <main>
        <Outlet />
      </main>
    </>
  );
}
