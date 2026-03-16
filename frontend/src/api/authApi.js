import api from './api';

export function setPassword(data) {
  return api({
    url: '/auth/password',
    method: 'post',
    data: data,
  });
}

export function login(data) {
  return api({
    url: '/auth/login',
    method: 'post',
    data: data,
  });
}
