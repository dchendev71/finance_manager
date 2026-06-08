import { createContext, useContext } from "react";

export type Currency = {
  code: string;
  name: string;
  symbol: string;
};

export interface UserProfile {
  email: string;
  currency: Currency;
  role: "ADMIN" | "USER";
  createdAt: "string";
  // updatedAt: "string" ? Is this needed?
}

export type UserContextType = {
  user: UserProfile | null;
  setUser: React.Dispatch<React.SetStateAction<UserProfile | null>>;
  clearUser: () => void;
};

export const UserContext = createContext<UserContextType | null>(null);

export const useUser = (): UserContextType => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error("useUser must be used within an UserProvider");
  }
  return context;
};
