import api from './api';
import { PAGINATION_SIZE } from '../helpers/constants';

export function getMeasurementTypes(page, search) {
  return api({
    url: '/measurement-types',
    method: 'get',
    params: {
      page: page - 1,
      size: PAGINATION_SIZE,
      search: search || undefined,
    },
  });
}

export function getMeasurementTypesForSelect() {
  return api({
    url: '/measurement-types/select',
    method: 'get',
  });
}

export function createMeasurementType(data) {
  return api({
    url: '/measurement-types',
    method: 'post',
    data,
  });
}
