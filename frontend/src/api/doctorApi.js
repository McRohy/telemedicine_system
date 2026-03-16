import api from './api';
import { PAGINATION_SIZE } from '../helpers/constants';

export function createDoctor(data) {
  return api({
    url: '/doctors',
    method: 'post',
    data,
  });
}

export function getDoctors(page, search) {
  return api({
    url: '/doctors',
    method: 'get',
    params: {
      page: page - 1, //React 1, Spring 0
      size: PAGINATION_SIZE,
      searchLastName: search || undefined,
    },
  });
}
