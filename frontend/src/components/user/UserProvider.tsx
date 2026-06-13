import { useCallback, useMemo, useState } from "react";
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

  const clearUser = useCallback(() => {
    setUser(null);
  }, []);

  const contextValue: UserContextType = useMemo(
    () => ({
      user,
      setUser,
      clearUser,
    }),
    [user, setUser, clearUser],
  );

  return (
    <UserContext.Provider value={contextValue}>{children}</UserContext.Provider>
  );
}
