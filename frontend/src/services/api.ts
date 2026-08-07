import { parseISO } from "date-fns";
import { formatInTimeZone } from "date-fns-tz";

const BASE_URL = import.meta.env.VITE_API_URL;
export const WS_URL = import.meta.env.VITE_WS_URL;

export interface CustomFetchOptions extends RequestInit {}

export async function customFetch(
  endpoint: string,
  token: string | null,
  options: CustomFetchOptions = {},
) {
  const headers: Record<string, string> = {
    "Content-Type": "application/json",
    ...normalizeHeaders(options.headers),
  };

  if (token != null) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const config: RequestInit = {
    ...options,
    headers,
  };

  // 3. Execute native fetch
  const response = await fetch(`${BASE_URL}${endpoint}`, config);

  return response;
}

function normalizeHeaders(headers?: HeadersInit): Record<string, string> {
  if (!headers) return {};
  if (headers instanceof Headers) {
    return Object.fromEntries(headers.entries());
  }
  if (Array.isArray(headers)) {
    return Object.fromEntries(headers);
  }
  return headers;
}

export function formatToUTCLibrary(ts: string | null): string {
  if (ts === null) {
    return "";
  }
  const isoString: string = ts as string;
  const date = parseISO(isoString);

  return formatInTimeZone(date, "UTC", "yyyy-MM-dd HH:mm");
}
