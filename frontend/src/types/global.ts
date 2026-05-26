export type RequestFunction = (
  endpoint: string,
  options?: RequestInit,
) => Promise<any>;
