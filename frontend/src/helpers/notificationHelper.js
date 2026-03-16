import { notifications } from '@mantine/notifications';

const POSITION = 'top-right';
const SUCCESS_COLOR = 'primary';
const ERROR_COLOR = 'red';

export function notifySuccess(title, message) {
  notifications.show({
    title: title,
    message: message,
    position: POSITION,
    color: SUCCESS_COLOR,
  });
}

export function notifyError(err) {
  notifications.show({
    title: "Chyba",
    message: err.response?.data?.message || "Niečo sa pokazilo, skúste to prosím znovu.",
    position: POSITION,
    color: ERROR_COLOR,
  });
}
