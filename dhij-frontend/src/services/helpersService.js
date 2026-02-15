
import { api } from "./apiClient";

// GET /helpers (requires USER or ADMIN)
export const getAllHelpers = async () => {
  const { data } = await api.get("/helpers");
  return data?.data || []; // unwrap ApiResponse<T>
};


export const getHelperById = async (id) => {
  const { data } = await api.get(`/helpers/${id}`);
  return data?.data;
};

// POST /helpers/add (requires ADMIN)
export const addHelper = async (payload) => {
  const { data } = await api.post("/helpers/add", payload);
  return data?.data; // newly created HelperDto
};

// PUT /helpers/{id} (requires auth)
export const updateHelper = async (id, payload) => {
  const { data } = await api.put(`/helpers/${id}`, payload);
  return data?.data;
};

// DELETE /helpers/{id} (requires ADMIN)
export const deleteHelper = async (id) => {
  const { data } = await api.delete(`/helpers/${id}`);
  return data;
};



export const getSlots = async (helperId, dateStr) => {
  const { data } = await api.get(`/helpers/${helperId}/slots`, { params: { date: dateStr } });
  return data?.data || [];
};

export const bookSlot = async (helperId, startIso) => {
  const { data } = await api.post(`/helpers/${helperId}/book`, { start: startIso });
  return data;
};

export const searchHelpers = async ({ name, role, specialty }) => {
  const params = new URLSearchParams();

  if (name) params.append("name", name);
  if (role) params.append("role", role);
  if (specialty) params.append("specialty", specialty);

  const { data } = await api.get(`/helpers/search?${params.toString()}`);
  return data?.data || [];
};


