import api from './api';

export function createMeasurementPlan(data) {
  return api({
    url: '/measurement-plans',
    method: 'post',
    data: data,
  });
}

export function updateMeasurementPlan(id, data) {
  return api({
    url: `/measurement-plans/${id}`,
    method: 'put',
    data: data,
  });
}

export function getMeasurementPlanByPersonalNumber(personalNumber) {
  return api({
    url: `/measurement-plans/${personalNumber}`,
    method: 'get',
  });
}
