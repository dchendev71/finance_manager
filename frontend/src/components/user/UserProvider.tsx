import { useState } from "react";
import {
  UserContext,
  type UserContextType,
  type UserProfile,
} from "./UserContext";

interface UserProviderProps {
  children: React.ReactNode;
}

export function UserProvider({ children }: UserProviderProps) {
  const [user, setUser] = useState<UserProfile | null>(null);

  function clearUser() {
    setUser(null);
  }

  const contextValue: UserContextType = {
    user: user,
    setUser: setUser,
    clearUser: clearUser,
  };

  return (
    <UserContext.Provider value={contextValue}>{children}</UserContext.Provider>
  );
}
