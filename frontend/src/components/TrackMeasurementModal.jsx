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
      personalNumber: plan.personalNumber,
      typeOfMeasurementId: '',
      value: '',
      note: '',
    },
  });

  console.log(plan);

  async function trackMeasurement() {
    console.log(form.values);
    setLoading(true);
    try {
      const res = await api.post('/measurements', form.values);
      notifySuccess(
        'Meranie zaznamenané',
        `${res.data.typeName} - ${res.data.value} ${res.data.units} je ${res.data.status}`,
      );
      onClose();
      form.reset();
    } catch (err) {
      console.log(err.response);
      const status = err.response?.status;

      if (status === 400) {
        form.setErrors(err.response.data.fieldErrors);
      } else {
        notifyError(err);
      }
    } finally {
      setLoading(false);
    }
  }

  if (!plan) return null;

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
      <Stack gap="md">
        <Select
          label="Typ merania"
          placeholder="Vyberte typ merania"
          data={plan.typesOfMeasurements.map((t) => ({
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

        <Group grow>
          <Button
            variant="outline"
            color="#0b5942"
            p="xs"
            size="md"
            onClick={() => form.reset()}
          >
            Zrušit
          </Button>
          <Button
            color="#0b5942"
            p="xs"
            size="md"
            loading={loading}
            onClick={() => trackMeasurement()}
          >
            Uložiť meranie
          </Button>
        </Group>
      </Stack>
    </Modal>
  );
}
