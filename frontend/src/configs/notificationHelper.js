import { notifications } from '@mantine/notifications';

const POSITION = 'top-right';
const SUCCESS_COLOR = '#0b5942';
const ERROR_COLOR = 'red';

export function notifySuccess(title, message) {
  notifications.show({ title, message, position: POSITION, color: SUCCESS_COLOR });
}

export function notifyError(err) {
  const status = err.response?.status;
  notifications.show({
    title: status,
    message: err.response?.data?.message,
    position: POSITION,
    color: ERROR_COLOR,
  });
}

export function notifyLoading(id, title, message) {
  notifications.show({ id, title, message, position: POSITION, color: SUCCESS_COLOR, loading: true });
}

export function notifyUpdate(id, title, message, color = SUCCESS_COLOR) {
  notifications.update({ id, title, message, position: POSITION, color, loading: false });
}
