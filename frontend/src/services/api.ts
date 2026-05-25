const BASE_URL = "http://localhost:8080/api/v1";

export async function customFetch(endpoint, token, options = {}) {
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
