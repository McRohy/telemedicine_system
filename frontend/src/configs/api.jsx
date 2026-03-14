import axios from 'axios';
/*
  https://axios-http.com/docs/instance
*/
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000, // to not wait infinitely for a response
});

/*
  https://axios-http.com/docs/interceptors
*/
api.interceptors.request.use(
   function (config) {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);

/*
  https://axios-http.com/docs/interceptors
*/
api.interceptors.response.use(
  function onFulfilled(response) {
    return response;
  },
  function onRejected(error) {
    const status = error.response?.status;
    const url = error.config?.url;
    if (status === 401 && url !== '/auth/login') {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;