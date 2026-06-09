import type { CallerFunction } from "@/types/global";
import type { Currency, UserProfile } from "./UserContext";
import type { Dispatch, SetStateAction } from "react";

export function fromUserResponseToUserProfile(response: any) {
  return {
    email: response.email,
    // TODO: Fix role and get it from the Backend
    role: "USER",
    createdAt: response.createdAt,
    currency: {
      code: response.currency.code,
      name: response.currency.name,
      symbol: response.currency.symbol,
    } as Currency,
  } as UserProfile;
}

export async function changePassword(
  formData: FormData,
  callerFn: CallerFunction,
  setUser: Dispatch<SetStateAction<UserProfile | null>>,
): Promise<any> {
  const currentPassword = formData.get("currentPassword") as string | null;
  const newPassword = formData.get("newPassword") as string | null;
  const confirmPassword = formData.get("confirmPassword") as string | null;

  if (!currentPassword || !newPassword || !confirmPassword) {
    callerFn.errorFn("All fields are required");
    return;
  }
  try {
    const response = await callerFn.requestFn(`/users/change-password`, {
      method: "POST",
      body: JSON.stringify({
        currentPassword: currentPassword,
        newPassword: newPassword,
        confirmPassword: confirmPassword,
      }),
    });
    if (response) {
      const userProfile = fromUserResponseToUserProfile(response);
      setUser(userProfile);
    }
  } catch (e: any) {
    callerFn.errorFn(e.message || "Network error - please try again");
    return;
  }
}

export async function changeEmail(
  formData: FormData,
  callerFn: CallerFunction,
  setUser: Dispatch<SetStateAction<UserProfile | null>>,
): Promise<any> {
  const currentPassword = formData.get("currentPassword") as string | null;
  const newEmail = formData.get("newEmail") as string | null;

  if (!currentPassword || !newEmail) {
    callerFn.errorFn("All fields are required");
    return;
  }
  try {
    const response = await callerFn.requestFn(`/users/change-email`, {
      method: "PATCH",
      body: JSON.stringify({
        currentPassword: currentPassword,
        newEmail: newEmail,
      }),
    });
    if (response) {
      const userProfile = fromUserResponseToUserProfile(response);
      setUser(userProfile);
    }
  } catch (e: any) {
    callerFn.errorFn(e.message || "Network error - please try again");
    return;
  }
}

export async function changeCurrency(
  formData: FormData,
  callerFn: CallerFunction,
  setUser: Dispatch<SetStateAction<UserProfile | null>>,
): Promise<any> {
  const currencyCode = formData.get("currencyCode") as string | "EUR";

  try {
    const response = await callerFn.requestFn(`/users/change-currency`, {
      method: "PATCH",
      body: JSON.stringify({
        currencyCode: currencyCode,
      }),
    });
    if (response) {
      const userProfile = fromUserResponseToUserProfile(response);
      setUser(userProfile);
    }
  } catch (e: any) {
    callerFn.errorFn(e.message || "Network error - please try again");
    return;
  }
}
