import { useState, useEffect } from 'react';
import { useForm } from '@mantine/form';
import { Modal, Stack, TextInput, Select, Button, MultiSelect } from '@mantine/core';
import { TimePicker } from '@mantine/dates';
import { notifySuccess, notifyError } from '../helpers/notificationHelper';
import { FREQUENCIES } from '../helpers/constants';
import { getMeasurementTypesForSelect } from '../api/measurementTypeApi';
import { createMeasurementPlan, updateMeasurementPlan } from '../api/measurementPlanApi';

export default function PlanModal({ opened, onClose, onSuccess, personalNumber, plan }) {
  const isEdit = Boolean(plan ? plan.id : null);
  console.log('From page:',plan, personalNumber);

  const [loading, setLoading] = useState(false);
  const [types, setTypes] = useState([]);
  const [typesLoading, setTypesLoading] = useState(true);
  const formRequest = useForm({
    initialValues: {
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
      } catch (error) {
        notifyError(error);
      } finally {
        setTypesLoading(false);
      }
    }
    fetchTypes();
  }, []);

  async function handleSubmit() {
    setLoading(true);
    try {
      if (isEdit) {
        console.log('Updating plan:', formRequest.values, plan.id);

        await updateMeasurementPlan(plan.id, formRequest.values);
        notifySuccess(
          'Plán upravený',
          'Monitorovací plán bol úspešne upravený.',
        );
      } else {
        console.log('Creating plan:', formRequest.values);
        const res = await createMeasurementPlan(formRequest.values);
        notifySuccess(
          'Plán meraní pridaný',
          `Pacientovi ${res.data.personalNumber} bol úspešne pridaný monitorovací plán.`,
        );
      }
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

  const numberOfTimePickers = formRequest.values.frequency === 'ONE_TIME_DAILY' ? 1 : formRequest.values.frequency === 'TWO_TIMES_DAILY' ? 2 : 0;

  return (
    <Modal
      opened={opened}
      onClose={() => { formRequest.reset(); onClose(); }}
      title={isEdit ? 'Upraviť monitorovací plán' : 'Vytvoriť monitorovací plán'}
    >
      <form onSubmit={formRequest.onSubmit(handleSubmit)}>
        <Stack gap="md">
          <TextInput
            label="Rodné číslo pacienta"
            type="text"
            ta="left"
            size="md"
            value={formRequest.values.personalNumber}
            disabled
          />

          <Select
            label="Frekvencia"
            placeholder="Vyberte frekvenciu"
            data={FREQUENCIES}
            size="md"
            searchable
            clearable
            value={formRequest.values.frequency}
            onChange={(value) => {
              formRequest.setFieldValue('frequency', value);
              formRequest.removeListItem('timesOfPlannedMeasurements', 1);
            }}
            error={formRequest.errors.frequency}
          />

          {numberOfTimePickers >= 1 && (
            <TimePicker
              label="Čas merania"
              withDropdown
              ta="left"
              size="md"
              {...formRequest.getInputProps('timesOfPlannedMeasurements.0')}
            />
          )}

          {numberOfTimePickers >= 2 && (
            <TimePicker
              label="Čas merania"
              withDropdown
              ta="left"
              size="md"
              {...formRequest.getInputProps('timesOfPlannedMeasurements.1')}
            />
          )}

          <MultiSelect
            label="Vyberte typy meraní"
            searchable
            clearable
            ta="left"
            size="md"
            disabled={typesLoading}
            placeholder={typesLoading ? 'Načítavanie...' : isEdit ? null : 'Vyberte typy meraní'}
            data={types.map((type) => ({ value: String(type.id), label: type.typeName }))}
            {...formRequest.getInputProps('typeOfMeasurementIds')}
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
