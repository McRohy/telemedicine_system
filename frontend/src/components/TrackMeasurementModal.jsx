import { useState } from 'react';
import { useForm } from '@mantine/form';
import {
  Modal,
  Stack,
  TextInput,
  Select,
  Button,
  NumberInput,
  Group,
} from '@mantine/core';
import { notifySuccess, notifyError } from '../configs/notificationHelper';
import api from '../configs/api';

export default function TrackMeasurementModal({ opened, onClose, plan }) {
  const [loading, setLoading] = useState(false);
  const form = useForm({
    initialValues: {
      personalNumber: plan?.personalNumber || '',
      typeOfMeasurementId: null,
      value: '',
      note: '',
    },
    validate: {
      typeOfMeasurementId: (value) => (value ? null : 'Vyberte typ merania'),
      value: (value) => (value === '' ? 'Zadejte hodnotu merania' : null),
    },
  });

  async function postMeasurement() {
    setLoading(true);
    try {
      const res = await api({
        url: '/measurements',
        method: 'post',
        data: form.values,
      });
      notifySuccess('Meranie zaznamenané', `${res.data.typeName} - ${res.data.value} ${res.data.units} je ${res.data.status}`);
      form.reset();
      onClose();
    } catch (error) {
      if (error.response?.status === 400) {
        form.setErrors(error.response.data.fieldErrors);
      } else {
        notifyError(error);
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <Modal
      opened={opened}
      onClose={onClose}
      title="Zaznamenať meranie"
      centered
      overlayProps={{
        backgroundOpacity: 0.8,
        blur: 5,
        color: '#0b5942',
      }}
    >
      <form onSubmit={form.onSubmit(postMeasurement)}>
        <Stack gap="md">
          <Select
            label="Typ merania"
            placeholder="Vyberte typ merania"
            data={plan?.typesOfMeasurements.map((t) => ({
              value: String(t.id),
              label: t.typeName,
            }))}
            size="md"
            searchable
            clearable
            withAsterisk
            {...form.getInputProps('typeOfMeasurementId')}
          />

          <NumberInput
            label="Hodnota merania"
            placeholder="Zadejte hodnotu merania"
            size="md"
            ta="left"
            withAsterisk
            {...form.getInputProps('value')}
          />

          <TextInput
            label="Poznámka"
            type="text"
            ta="left"
            size="md"
            {...form.getInputProps('note')}
          />

          <Button
            type="submit"
            color="#0b5942"
            p="xs"
            size="md"
            loading={loading}
          >
            Uložiť meranie
          </Button>
        </Stack>
      </form>
    </Modal>
  );
}
