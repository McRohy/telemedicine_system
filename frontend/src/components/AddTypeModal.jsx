import { useState } from 'react';
import { Modal, Stack, TextInput, Button, NumberInput } from '@mantine/core';
import { notifySuccess, notifyError } from '../helpers/notificationHelper';
import { createMeasurementType } from '../api/measurementTypeApi';
import { useForm } from '@mantine/form';

export default function AddTypeModal({ opened, onClose, onSuccess }) {
  const [loading, setLoading] = useState(false);
  const formRequest = useForm({
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
      const res = await createMeasurementType(formRequest.values);
      notifySuccess(
        'Typ merania pridaný',
        `${res.data.typeName} s min: ${res.data.minValue} a max: ${res.data.maxValue} jednotkou ${res.data.units} bol úspešne pridaný.`,
      );
      formRequest.reset();
      onClose();
      onSuccess();
    } catch (error) {
      console.log(error.response);
      const status = error.response?.status;

      if (status === 400) {
        formRequest.setErrors(error.response.data.fieldErrors);
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
        formRequest.reset();
        onClose();
      }}
      title="Pridať typ merania"
    >
      <form onSubmit={formRequest.onSubmit(handleCreateType)}>
        <Stack gap="md">
          <TextInput
            label="Názov typu merania"
            placeholder="váha, tlak..."
            type="text"
            ta="left"
            size="md"
            withAsterisk
            {...formRequest.getInputProps('typeName')}
          />

          <TextInput
            label="Jednotky"
            placeholder="kg, cm, etc..."
            ta="left"
            size="md"
            withAsterisk
            {...formRequest.getInputProps('units')}
          />
          <NumberInput
            label="Minimálna hodnota"
            placeholder="0"
            ta="left"
            size="md"
            min={0}
            withAsterisk
            {...formRequest.getInputProps('minValue')}
          />

          <NumberInput
            label="Maximálna hodnota"
            placeholder="100"
            ta="left"
            size="md"
            min={0}
            withAsterisk
            {...formRequest.getInputProps('maxValue')}
          />

          <Button type="submit" p="xs" size="md" loading={loading}>
            Pridať typ merania
          </Button>
        </Stack>
      </form>
    </Modal>
  );
}
