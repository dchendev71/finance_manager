import { useCallback, useMemo, useState } from "react";
import { AuthContext } from "./AuthContext";
import { customFetch } from "@/services/api";
import { useNavigate } from "react-router-dom";

import type { AuthContextType } from "./AuthContext";
import { useUser, type UserProfile } from "@/components/user/UserContext";

// Define props to accept standard nested React children nodes
interface AuthProviderProps {
  children: React.ReactNode;
}

export function AuthProvider({ children }: AuthProviderProps) {
  // Initialize state using a type union (string or null)
  const [token, setToken] = useState<string | null>(null);
  const navigate = useNavigate();
  const { setUser, clearUser } = useUser();

  const logout = useCallback(() => {
    clearUser();
    setToken(null);
    navigate("/login");
  }, [clearUser, navigate]);

  const login = useCallback(
    (token: string, user: UserProfile) => {
      setUser(user);
      setToken(token);
    },
    [setUser],
  );

  const request = useCallback(
    async (endpoint: string, options: RequestInit = {}): Promise<any> => {
      try {
        const res = await customFetch(endpoint, token as string, options);

        if (!res.ok) {
          const errorData = await res.json().catch(() => ({}));
          throw new Error(
            errorData.message || `HTTP error! Status: ${res.status}`,
          );
        }

        // Automatically parse json unless it's a 204 No Content
        return res.status !== 204 ? await res.json() : null;
      } catch (error) {
        throw error;
      }
    },
    [token],
  );
  const contextValue: AuthContextType = useMemo(
    () => ({
      token,
      login,
      logout,
      request,
      isAuthenticated: !!token,
    }),
    [token, login, logout, request],
  );

  return (
    <AuthContext.Provider value={contextValue}>{children}</AuthContext.Provider>
  );
}
