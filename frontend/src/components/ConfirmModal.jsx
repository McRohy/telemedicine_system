import { Modal, Stack, Text, Button, Group } from '@mantine/core';

/** 
 * Modal for confirming an action. 
 * Contains a message and buttons to confirm or cancel the action.
 */
export default function ConfirmModal({ opened, onClose, onConfirm, title, message, buttonText}) {
  return (
    <Modal opened={opened} onClose={onClose} title={title} size="sm">
      <Stack gap="md">
        <Text size="sm">{message}</Text>
        <Group justify="flex-end">
          <Button variant="default" onClick={onClose}>
            Zrušiť
          </Button>
          <Button color="red" onClick={onConfirm}>
            {buttonText}
          </Button>
        </Group>
      </Stack>
    </Modal>
  );
}
