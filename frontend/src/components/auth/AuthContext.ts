import { createContext, useContext } from "react";

// 1. Define the TypeScript shape of our Context data cloud
export type AuthContextType = {
  token: string | null;
  isAuthenticated: boolean;
  login: (newToken: string) => void;
  logout: () => void;
  request: (endpoint: string, options?: RequestInit) => Promise<any>;
};

// 2. Initialize with null, but cast to AuthContextType so consumers don't have to check for null constantly
export const AuthContext = createContext<AuthContextType | null>(null);

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};
