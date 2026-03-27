import axios from 'axios';
/*
  Axios instance for all API requests.
  https://axios-http.com/docs/instance
  https://axios-http.com/docs/interceptors
*/
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000, // to not wait infinitely for a response
});

/**
 * Attaches JWT Bearer token from localStorage to every request.
 */
api.interceptors.request.use(
   function (config) {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
);

/**
 * Handles API responses.
 * 401 clears credentials and redirects to login page.
 * 403 redirects to forbidden page.
 * Other errors are handled locally in components.
 */
api.interceptors.response.use(
  function handleSuccessfulResponse(response) {
    return response;
  },

  function handleRejectedResponse(error) {
    const status = error.response?.status;
    const url = error.config?.url;
    if (status === 401 && url !== '/auth/login') {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    if (status === 403) {
      window.location.href = '/forbidden';
    }
    return Promise.reject(error);
  }
);

export default api;