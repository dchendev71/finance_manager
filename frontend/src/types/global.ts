export type RequestFunction = (
  endpoint: string,
  options?: RequestInit,
) => Promise<any>;

export type CallerFunction = {
  requestFn: RequestFunction;
  errorFn: (message: string) => void;
};
