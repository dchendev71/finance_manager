import type { CallerFunction } from "@/types/global";

export async function createPortfolio(
  formData: FormData,
  callerFn: CallerFunction,
): Promise<any> {
  const portfolioName = formData.get("portfolioName") as string | null;
  if (!portfolioName) {
    callerFn.errorFn("Portfolio name is a required field");
    return;
  }
  try {
    const response = await callerFn.requestFn("/portfolios", {
      method: "POST",
      body: JSON.stringify({
        portfolioName: portfolioName,
      }),
    });

    return response;
  } catch (e: any) {
    callerFn.errorFn(e.message || "Network error - please try again");
  }
}

export async function getPortfolios(callerFn: CallerFunction): Promise<any> {
  try {
    const response = await callerFn.requestFn("/portfolios", {
      method: "GET",
    });
    return response;
  } catch (e: any) {
    callerFn.errorFn(e.message || "Network error - please try again");
  }
}
