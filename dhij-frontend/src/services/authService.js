
import { api } from "./apiClient";

// Returns: { token }
export const loginApi = async (payload) => {
  const { data } = await api.post("/auth/login", payload);
  return data; // { token }
};

// Optional: call your register endpoint
export const registerApi = async (payload) => {
  const { data } = await api.post("/auth/register", payload);
  return data; // { token }
};
