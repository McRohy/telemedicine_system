import axios from 'axios';
/*
  https://axios-http.com/docs/instance
  https://axios-http.com/docs/interceptors
*/
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000, // to not wait infinitely for a response
});

// add token to every request
api.interceptors.request.use(
   function (config) {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
);

// handle response and logout on 401 globally
// other errors are handling locally in request
api.interceptors.response.use(
  function onFulfilled(response) {
    return response;
  },
  function onRejected(error) {
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