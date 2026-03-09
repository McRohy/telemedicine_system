import axios from 'axios';

// Axios instance with a common base URL
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
    const url = error.config?.url;
    if (status === 401 && url !== '/auth/login') {
      localStorage.removeItem('token');
      window.location.href = '/login'; //full page reload/reset 
    }
    throw error;
  }
);

export default api;
