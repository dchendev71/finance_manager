import { NavLink } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

interface NavLinkRenderProps {
  isActive: boolean;
  isPending?: boolean;
}

export default function Navbar() {
  const { logout } = useAuth();
  // Reusable styling function leveraging React Router's isActive state
  const linkClasses = ({ isActive }: NavLinkRenderProps) =>
    `px-4 py-2 rounded-lg text-sm font-semibold transition-all duration-200 ${
      isActive
        ? "bg-slate-950 text-emerald-400 shadow-inner"
        : "text-slate-300 hover:bg-slate-700/50 hover:text-white"
    }`;

  const handleLogout = () => {
    // TODO: Wire up your AuthProvider/UserProvider logout function here
    console.log("Logging out user...");
    logout();
  };

  return (
    <nav className="bg-slate-900 border-b border-slate-800 shadow-md sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-6 h-16 flex items-center justify-between">
        {/* Left Section: Brand / Platform Logo */}
        <div className="flex items-center gap-2 select-none">
          <div className="h-8 w-8 rounded-md bg-emerald-500 flex items-center justify-center font-black text-slate-950 text-lg">
            A
          </div>
          <span className="text-white font-bold text-lg tracking-tight">
            ALPHA<span className="text-emerald-400">VEST</span>
          </span>
        </div>

        {/* Center Section: Core Application Navigation Routes */}
        <div className="flex items-center space-x-2">
          <NavLink to="/home" className={linkClasses}>
            Dashboard
          </NavLink>
          <NavLink to="/performance" className={linkClasses}>
            Performance
          </NavLink>
          <NavLink to="/profile" className={linkClasses}>
            Profile
          </NavLink>
        </div>

        {/* Right Section: System / Destructive Actions */}
        <div className="flex items-center">
          <button
            onClick={handleLogout}
            className="px-4 py-2 text-sm font-semibold text-slate-400 hover:text-rose-400 rounded-lg hover:bg-rose-950/20 transition-all duration-200"
          >
            Logout
          </button>
        </div>
      </div>
    </nav>
  );
}
