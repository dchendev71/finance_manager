import { useState } from "react";
import { AuthContext } from "./AuthContext";
import { customFetch } from "@/services/api";
import { useNavigate } from "react-router-dom";

import type { AuthContextType } from "./AuthContext";

// Define props to accept standard nested React children nodes
interface AuthProviderProps {
  children: React.ReactNode;
}

export function AuthProvider({ children }: AuthProviderProps) {
  // Initialize state using a type union (string or null)
  const [token, setToken] = useState<string | null>(null);
  const navigate = useNavigate();

  const login = (newToken: string): void => setToken(newToken);
  const logout = (): void => setToken(null);

  // Use standard native RequestInit type for native fetch configurations
  async function request(
    endpoint: string,
    options: RequestInit = {},
  ): Promise<any> {
    try {
      const res = await customFetch(endpoint, token, options);

      // Intercept 401 errors globally
      if (res.status === 401) {
        logout();
        navigate("/login");
        return null;
      }

      if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw new Error(
          errorData.message || `HTTP error! Status: ${res.status}`,
        );
      }

      // Automatically parse json unless it's a 204 No Content
      return res.status !== 204 ? await res.json() : null;
    } catch (error) {
      console.error("Auth Request Failure:", error);
      throw error;
    }
  }

  // Create the value object ensuring it adheres precisely to our structural contract
  const contextValue: AuthContextType = {
    token,
    login,
    logout,
    request,
    isAuthenticated: !!token,
  };

  return (
    <AuthContext.Provider value={contextValue}>{children}</AuthContext.Provider>
  );
}
