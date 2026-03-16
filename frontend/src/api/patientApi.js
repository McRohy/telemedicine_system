import api from './api';
import { PAGINATION_SIZE } from '../helpers/constants';

export function createPatient(data) {
  return api({
    url: '/patients',
    method: 'post',
    data,
  });
}

export function getPatients(page, search) {
  return api({
    url: '/patients',
    method: 'get',
    params: {
      page: page - 1,
      size: PAGINATION_SIZE,
      searchLastName: search || undefined,
    },
  });
}

export function getPatientsByPanNumber(panNumber, page, search) {
  return api({
    url: '/patients',
    method: 'get',
    params: {
      panNumber,
      page: page - 1,
      size: PAGINATION_SIZE,
      searchLastName: search || undefined,
    },
  });
}

export function getPatientByPersonalNumber(personalNumber) {
  return api({
    url: `/patients/${personalNumber}`,
    method: 'get',
  });
}
