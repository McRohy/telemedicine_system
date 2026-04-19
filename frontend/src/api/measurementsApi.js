import { PAGINATION_SIZE } from '../helpers/constants';
import api from './api';

export function postMeasurement(data) {
  return api({
    url: '/measurements',
    method: 'post',
    data: data,
  });
}

export function getAllFilteredMeasurements(personalNumber, filterType, period) {
  return api({
    url: '/measurements/progress',
    method: 'get',
    params: {
      personalNumber,
      typeId: filterType,
      period,
    },
  });
}

export function getAllMeasurementsForTable(personalNumber, filterType, page) {
  return api({
    url: '/measurements',
    method: 'get',
    params: {
      personalNumber,
      page: page - 1, //React 1, Spring 0
      size: PAGINATION_SIZE,
      typeId: filterType || undefined,
    },
  });
}
