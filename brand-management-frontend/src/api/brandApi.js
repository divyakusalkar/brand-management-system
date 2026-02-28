import axios from 'axios';

const API_BASE = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE,
  headers: { 'Content-Type': 'application/json' },
});

/* ── Brand APIs ──────────────────────────────────────────────────────── */

export const getAllBrands = (chainId) => {
  const params = chainId ? { chainId } : {};
  return api.get('/brands', { params });
};

export const getBrandById = (id) => api.get(`/brands/${id}`);

export const createBrand = (data) => api.post('/brands', data);

export const updateBrand = (id, data) => api.put(`/brands/${id}`, data);

export const deleteBrand = (id) => api.delete(`/brands/${id}`);

/* ── Chain APIs ──────────────────────────────────────────────────────── */

export const getAllChains = () => api.get('/chains');
