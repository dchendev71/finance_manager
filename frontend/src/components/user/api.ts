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

interface UpdateUserConfig {
  endpoint: string;
  method: "POST" | "PATCH";
  requiredFields?: string[];
}

export type UpdateUserPayload = {
  formData: FormData;
  callerFn: CallerFunction;
  setUser: Dispatch<SetStateAction<UserProfile | null>>;
  config: UpdateUserConfig;
};

export async function updateUser({
  formData,
  callerFn,
  setUser,
  config,
}: UpdateUserPayload): Promise<any> {
  // We dynamically fill the payload based on the requiredFields
  const payload: Record<string, string> = {};
  const fields = config.requiredFields || Array.from(formData.keys());
  for (const field of fields) {
    const value = formData.get(field) as string | null;
    if (!value && config.requiredFields?.includes(field)) {
      callerFn.errorFn("All fields are required");
      return;
    }
    if (value) payload[field] = value;
  }

  try {
    const response = await callerFn.requestFn(config.endpoint, {
      method: config.method,
      body: JSON.stringify(payload),
    });

    if (response) {
      const userProfile: UserProfile = fromUserResponseToUserProfile(response);
      setUser(userProfile);
    }
  } catch (e: any) {
    callerFn.errorFn(e.message || "Network error - please try again");
    return;
  }
}
