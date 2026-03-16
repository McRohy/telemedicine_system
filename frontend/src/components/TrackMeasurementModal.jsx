import { useState } from 'react';
import { useForm } from '@mantine/form';
import { Modal, Stack, TextInput, Select, Button, NumberInput } from '@mantine/core';
import { notifySuccess, notifyError } from '../helpers/notificationHelper';
import { postMeasurement } from '../api/measurementsApi';

export default function TrackMeasurementModal({ opened, onClose, onSuccess, plan,}) {
  const [loading, setLoading] = useState(false);
  const form = useForm({
    initialValues: {
      personalNumber: plan?.personalNumber || '',
      typeOfMeasurementId: null,
      value: '',
      note: '',
    },
    validate: {
      typeOfMeasurementId: (value) => (value ? null : 'Povinné pole'),
      value: (value) => (value !== '' ? null : 'Povinné pole'),
    },
  });

  async function handleTrackMeasurement() {
    setLoading(true);
    try {
      const res = await postMeasurement(form.values);
      notifySuccess(
        'Meranie zaznamenané',
        `${res.data.typeName} - ${res.data.value} ${res.data.units} je ${res.data.status}`,
      );
      form.reset();
      onClose();
      onSuccess();
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
      onClose={() => {
        form.reset();
        onClose();
      }}
      title="Zaznamenať meranie"
    >
      <form onSubmit={form.onSubmit(handleTrackMeasurement)}>
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

          <Button type="submit" p="xs" size="md" loading={loading}>
            Uložiť meranie
          </Button>
        </Stack>
      </form>
    </Modal>
  );
}
