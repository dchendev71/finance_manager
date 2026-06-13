import { parseISO } from "date-fns";
import { formatInTimeZone } from "date-fns-tz";

const BASE_URL = "http://localhost:8080/api/v1";

export async function customFetch(
  endpoint: string,
  token: string | null,
  options = {},
) {
  const headers = {
    "Content-Type": "application/json",
    ...options.headers,
  };

  if (token != null) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const config = {
    ...options,
    headers,
  };

  // 3. Execute native fetch
  const response = await fetch(`${BASE_URL}${endpoint}`, config);

  return response;
}

export function formatToUTCLibrary(ts: string | null): string {
  if (ts === null) {
    return "";
  }
  const isoString: string = ts as string;
  const date = parseISO(isoString);

  return formatInTimeZone(date, "UTC", "yyyy-MM-dd HH:mm");
}
