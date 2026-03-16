import { useState, useEffect } from 'react';
import { useForm } from '@mantine/form';
import { Modal, Stack, TextInput, Select, Button, Group, MultiSelect } from '@mantine/core';
import { TimePicker } from '@mantine/dates';
import { notifySuccess, notifyError } from '../helpers/notificationHelper';
import { FREQUENCIES } from '../helpers/constants';
import { getMeasurementTypesForSelect } from '../api/measurementTypeApi';
import { createMeasurementPlan, updateMeasurementPlan } from '../api/measurementPlanApi';

export default function PlanModal({ opened, onClose, onSuccess, panNumber, personalNumber, plan }) {
  const isEdit = Boolean(plan ? plan.id : null);
  console.log('From page:',plan, panNumber, personalNumber);

  const [loading, setLoading] = useState(false);
  const [types, setTypes] = useState([]);
  const form = useForm({
    initialValues: {
      panNumber: panNumber,
      personalNumber: personalNumber,
      frequency: (isEdit ? plan.frequency : null),
      timesOfPlannedMeasurements: (isEdit ? plan.timesOfPlannedMeasurements : []),
      typeOfMeasurementIds: (isEdit ? plan.typesOfMeasurements.map(t => String(t.id)) : []), //Jackson will transform to integers
    },
    validate: {
      frequency: (value) => (value ? null : 'Povinné pole'),
      typeOfMeasurementIds: (value) => (value.length > 0 ? null : 'Povinné pole'),
      timesOfPlannedMeasurements: (value) => (value.length > 0 ? null : 'Povinné pole'),
    },
  });

  useEffect(() => {
    async function fetchTypes() {
      try {
        const response = await getMeasurementTypesForSelect();
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
      if (isEdit) {
        console.log('Updating plan:', form.values);
        await updateMeasurementPlan(plan.id, form.values);
        notifySuccess(
          'Plan upraveny',
          'Monitorovaci plan bol uspesne upraveny.',
        );
      } else {
        const res = await createMeasurementPlan(form.values);
        notifySuccess(
          'Plan merani pridany',
          `Pacient ${res.data.personalNumber} ma plan s frekvenciou ${res.data.frequency} a casom planovanych merani ${res.data.timesOfPlannedMeasurements}.`,
        );
      }
      onClose();
      onSuccess();
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
            data={FREQUENCIES}
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
