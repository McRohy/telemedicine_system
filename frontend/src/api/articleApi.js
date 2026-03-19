import api from './api';
import { PAGINATION_SIZE } from '../helpers/constants';

export function createArticle(data) {
  return api({
    url: '/articles',
    method: 'post',
    data,
  });
}

export function getArticlesByPanNumber(panNumber, page) {
  return api({
    url: '/articles',
    method: 'get',
    params: {
      panNumber,
      page: page - 1,
      size: PAGINATION_SIZE,
    },
  });
}

export function getArticles(page) {
  return api({
    url: '/articles',
    method: 'get',
    params: {
      page: page - 1,
      size: PAGINATION_SIZE,
    },
  });
}

export function getArticleById(id) {
  return api({
    url: `/articles/${id}`,
    method: 'get',
  });
}

export function deleteArticle(id) {
  return api({
    url: `/articles/${id}`,
    method: 'delete',
  });
}