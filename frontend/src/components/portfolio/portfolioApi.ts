import type { RequestFunction } from "@/types/global";

export async function createPortfolio(
  formData: FormData,
  requestFn: RequestFunction,
): Promise<any> {
  const portfolioName = formData.get("portfolioName") as string | null;
  if (!portfolioName) {
    throw new Error("Portfolio name is a required field");
  }
  try {
    const response = await requestFn("/portfolio/create", {
      method: "POST",
      body: JSON.stringify({
        portfolioName: portfolioName,
      }),
    });

    return response;
  } catch (e: any) {
    throw new Error(e.message || "Network error - please try again");
  }
}

export async function getPortfolios(requestFn: RequestFunction): Promise<any> {
  try {
    const response = await requestFn("/portfolio/list", {
      method: "GET",
    });
    return response;
  } catch (e: any) {
    throw new Error(e.message || "Network error - please try again");
  }
}
