import api from './api';

export function setPassword({ password, token }) {
  return api({
    url: '/auth/password',
    method: 'post',
    data: { password, token },
  });
}

export function login(data) {
  return api({
    url: '/auth/login',
    method: 'post',
    data: data,
  });
}
