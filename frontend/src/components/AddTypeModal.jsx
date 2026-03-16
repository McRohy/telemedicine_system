import { useState } from 'react';
import { Modal, Stack, TextInput, Button, NumberInput } from '@mantine/core';
import { notifySuccess, notifyError } from '../helpers/notificationHelper';
import { createMeasurementType } from '../api/measurementTypeApi';
import { useForm } from '@mantine/form';

export default function AddTypeModal({ opened, onClose, onSuccess }) {
  const [loading, setLoading] = useState(false);
  const form = useForm({
    initialValues: {
      typeName: '',
      units: '',
      minValue: '',
      maxValue: '',
    },
    validate: {
      typeName: (value) => (value ? null : 'Povinné pole'),
      units: (value) => (value ? null : 'Povinné pole'),
      minValue: (value) => (value !== '' ? null : 'Povinné pole'),
      maxValue: (value) => (value !== '' ? null : 'Povinné pole'),
    },
  });

  async function handleCreateType() {
    setLoading(true);
    try {
      const res = await createMeasurementType(form.values);
      notifySuccess(
        'Typ merania pridaný',
        `${res.data.typeName} s min: ${res.data.minValue} a max: ${res.data.maxValue} jednotkou ${res.data.units} bol úspešne pridaný.`,
      );
      form.reset();
      onClose();
      onSuccess();
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

  return (
    <Modal
      opened={opened}
      onClose={() => {
        form.reset();
        onClose();
      }}
      title="Pridať typ merania"
    >
      <form onSubmit={form.onSubmit(handleCreateType)}>
        <Stack gap="md">
          <TextInput
            label="Názov typu merania"
            placeholder="váha, tlak..."
            type="text"
            ta="left"
            size="md"
            withAsterisk
            {...form.getInputProps('typeName')}
          />

          <TextInput
            label="Jednotky"
            placeholder="kg, cm, etc..."
            ta="left"
            size="md"
            withAsterisk
            {...form.getInputProps('units')}
          />
          <NumberInput
            label="Minimálna hodnota"
            placeholder="0"
            ta="left"
            size="md"
            min={0}
            withAsterisk
            {...form.getInputProps('minValue')}
          />

          <NumberInput
            label="Maximálna hodnota"
            placeholder="100"
            ta="left"
            size="md"
            min={0}
            withAsterisk
            {...form.getInputProps('maxValue')}
          />

          <Button type="submit" p="xs" size="md" loading={loading}>
            Pridať typ merania
          </Button>
        </Stack>
      </form>
    </Modal>
  );
}
