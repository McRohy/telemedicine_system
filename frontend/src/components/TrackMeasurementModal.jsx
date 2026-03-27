import { useState } from 'react';
import { useForm } from '@mantine/form';
import { Modal, Stack, TextInput, Select, Button, NumberInput, Group } from '@mantine/core';
import { notifySuccess, notifyError } from '../helpers/notificationHelper';
import { postMeasurement } from '../api/measurementsApi';

/** 
 * Modal for tracking a new measurement. 
 * Contains a form with measurement details and a submit button to add the measurement.
 */
export default function TrackMeasurementModal({ opened, onClose, onSuccess, plan,}) {
  const [loading, setLoading] = useState(false);
  const formRequest = useForm({
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
      const res = await postMeasurement(formRequest.values);
      notifySuccess(
        'Meranie zaznamenané',
        `${res.data.typeName} - ${res.data.value} ${res.data.units} je 
         ${res.data.status === 'NORMAL' ? 'v norme' : 'mimo normy - lekár bol informovaný notifikáciou'}`,
      );
      formRequest.reset();
      onClose();
      onSuccess();
    } catch (error) {
      if (error.response?.status === 400) {
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
      title="Zaznamenať meranie"
    >
      <form onSubmit={formRequest.onSubmit(handleTrackMeasurement)}>
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
            {...formRequest.getInputProps('typeOfMeasurementId')}
          />

          <Group grow>
            <NumberInput
              label="Hodnota merania"
              placeholder="Zadajte hodnotu"
              size="md"
              ta="left"
              withAsterisk
              {...formRequest.getInputProps('value')}
            />

            <TextInput
              label="Jednotka"
              type="text"
              ta="left"
              size="md"
              value={plan?.typesOfMeasurements.find((t) => String(t.id) === formRequest.values.typeOfMeasurementId)?.units || ''}
              disabled
            />
          </Group>

          <TextInput
            label="Poznámka"
            type="text"
            ta="left"
            size="md"
            {...formRequest.getInputProps('note')}
          />

          <Button type="submit" p="xs" size="md" loading={loading}>
            Uložiť meranie
          </Button>
        </Stack>
      </form>
    </Modal>
  );
}
