import type { CallerFunction } from "@/types/global";

export async function fetchBalance(callerFn: CallerFunction): Promise<any> {
  try {
    const response = await callerFn.requestFn("/balance");
    return response;
  } catch (e: any) {
    callerFn.errorFn(e.message || "Network error - please try again");
    return;
  }
}

export async function increaseBalance(
  callerFn: CallerFunction,
  increaseAmount: number,
): Promise<any> {
  try {
    const response = await callerFn.requestFn("/balance/increase", {
      method: "POST",
      body: JSON.stringify({
        increaseAmount: increaseAmount,
      }),
    });

    return response;
  } catch (e: any) {
    callerFn.errorFn(e.message || "Network error - please try again");
  }
}
