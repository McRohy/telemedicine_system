import api from './api';

export function createMeasurementPlan(data) {
  return api({
    url: '/measurement-plans',
    method: 'post',
    data: data,
  });
}

export function updateMeasurementPlan(planId, data) {
  return api({
    url: `/measurement-plans/${planId}`,
    method: 'put',
    data: data,
  });
}

export function getMeasurementPlanByPersonalNumber(personalNumber) {
  return api({
    url: '/measurement-plans',
    method: 'get',
    params: { personalNumber },
  });
}
