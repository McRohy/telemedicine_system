import { useState, useEffect } from 'react';
import { useForm } from '@mantine/form';
import { Modal, Stack, TextInput, Select, Button, Group, MultiSelect } from '@mantine/core';
import { TimePicker } from '@mantine/dates';
import { notifySuccess, notifyError } from '../configs/notificationHelper';
import frequency from '../constants/frequency';
import api from '../configs/api';

export default function PlanModal({ opened, onClose, panNumber, personalNumber, plan }) {
  const isEdit = Boolean(plan ? plan.id : null);
  
  const [loading, setLoading] = useState(false);
  const [types, setTypes] = useState([]);
  const form = useForm({
    initialValues: {
      panNumber: (isEdit ? plan.panNumber : panNumber),
      personalNumber: (isEdit ? plan.personalNumber : personalNumber),
      frequency: (isEdit ? plan.frequency : null),
      timesOfPlannedMeasurements: (isEdit ? plan.timesOfPlannedMeasurements : []),
      typeOfMeasurementIds: (isEdit ? plan.typesOfMeasurements.map(t => String(t.id)) : []),
    },
    validate: {
      frequency: (value) => (value ? null : 'Vyberte frekvenciu'),
      typeOfMeasurementIds: (value) => (value.length > 0 ? null : 'Vyberte aspoň jeden typ merania'),
      timesOfPlannedMeasurements: (value) => (value.length > 0 ? null : 'Vyberte aspon jeden čas merania'),
    }
  });

  useEffect(() => {
    async function fetchTypes() {
      try {
        const response = await api({
          url: '/measurement-types',
          method: 'get',
        });
        setTypes(response.data);
      } catch (err) {
        notifyError(err);
      }
    }
    fetchTypes();
  }, []);

  async function handleSubmit() {
    setLoading(true);
    try {
      const payload = {
        ...form.values,
        typeOfMeasurementIds: form.values.typeOfMeasurementIds.map(Number),
     };

      if (isEdit) {
        console.log('Updating plan with payload:', payload);
        await api({
          url: `/measurement-plans/${plan.id}`,
          method: 'put',
          data: payload,
        });
        notifySuccess('Plan upraveny', 'Monitorovaci plan bol uspesne upraveny.');
      } else {
        const res = await api({
          url: '/measurement-plans',
          method: 'post',
          data: payload,
        });
        notifySuccess(
          'Plan merani pridany',
          `Pacient ${res.data.personalNumber} ma plan s frekvenciou ${res.data.frequency} a casom planovanych merani ${res.data.timesOfPlannedMeasurements}.`,
        );
      }
      onClose();
    } catch (err) {
      if (err.response?.status === 400) {
        form.setErrors(err.response.data.fieldErrors);
      } else {
        notifyError(err);
      }
    } finally {
      setLoading(false);
    }
  }

  const numberOfTimePickers = form.values.frequency === 'ONE_TIME_DAILY' ? 1 : form.values.frequency === 'TWO_TIMES_DAILY' ? 2 : 0;

  return (
    <Modal
      opened={opened}
      onClose={onClose}
      title={isEdit ? 'Upravit monitorovaci plan' : 'Vytvorit monitorovaci plan'}
    >
      <form onSubmit={form.onSubmit(handleSubmit)}>
        <Stack gap="md">
          <Group grow>
            <TextInput
              label="PAN cislo lekara"
              type="text"
              ta="left"
              size="md"
              value={form.values.panNumber}
              disabled
            />

            <TextInput
              label="Rodne cislo pacienta"
              type="text"
              ta="left"
              size="md"
              value={form.values.personalNumber}
              disabled
            />
          </Group>

          <Select
            label="Frekvencia"
            placeholder="Vyberte frekvenciu"
            data={frequency}
            size="md"
            searchable
            clearable
            value={form.values.frequency}
            onChange={(value) => {
        
              form.setFieldValue('frequency', value);
              form.removeListItem('timesOfPlannedMeasurements', 1);
            }}
            error={form.errors.frequency}
          />

          {numberOfTimePickers >= 1 && (
            <TimePicker
              label="Cas merania 1"
              withDropdown
              ta="left"
              size="md"
              {...form.getInputProps('timesOfPlannedMeasurements.0')}
            />
          )}

          {numberOfTimePickers >= 2 && (
            <TimePicker
              label="Cas merania 2"
              withDropdown
              ta="left"
              size="md"
              {...form.getInputProps('timesOfPlannedMeasurements.1')}
            />
          )}

          <MultiSelect
            label="Vyberte typy merani"
            searchable
            clearable
            ta="left"
            size="md"
            data={types.map((type) => ({ value: String(type.id), label: type.typeName }))}
            {...form.getInputProps('typeOfMeasurementIds')}
          />

          <Button
            type="submit"
            p="xs"
            size="md"
            loading={loading}
          >
            {isEdit ? 'Uložiť plán' : 'Vytvoriť plán'}
          </Button>
        </Stack>
      </form>
    </Modal>
  );
}
