import { useState, useEffect } from 'react';
import {
  Modal,
  Stack,
  TextInput,
  Select,
  Button,
  Group,
  MultiSelect,
  Loader,
  Center,
} from '@mantine/core';
import { TimePicker } from '@mantine/dates';
import { notifySuccess, notifyError } from '../configs/notificationHelper';
import frequency from '../constants/frequency';
import api from '../configs/api';

export default function EditPlanModal({ opened, onClose, plan }) {
  const [loading, setLoading] = useState(false);

  const [types, setTypes] = useState([]);
  const [planUpdate, setPlanUpdate] = useState({
    frequency: plan?.frequency || null,
    typeOfMeasurementIds: (plan?.typeOfMeasurementIds ?? []).map(String), //to convert to string for MultiSelect
    timesOfPlannedMeasurements: plan?.timesOfPlannedMeasurements || [],
    panNumber: plan?.panNumber || '',
    personalNumber: plan?.personalNumber || '',
  });
  const [errorInputs, setErrorInputs] = useState(null);

  useEffect(() => {
    async function fetchTypes() {
      try {
        const response = await api.get('/measurement-types');
        console.log('measurement-types response:', response.data);
        setTypes(response.data);
      } catch (err) {
        console.error(err);
      }
    }
    fetchTypes();
  }, []);

  async function updatePlan() {
    setLoading(true);
    try {
      console.log('Updating plan with data:', planUpdate);
      await api.put(`/measurement-plans/${plan.id}`, {
        ...planUpdate,
        typeOfMeasurementIds: planUpdate.typeOfMeasurementIds.map(Number),
      });
      notifySuccess('Plán upravený', 'Monitorovací plán bol úspešne upravený.');
      onClose();
    } catch (err) {
      console.log(err.response);
      const status = err.response?.status;

      if (status === 400) {
        setErrorInputs(err.response.data.fieldErrors);
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
      onClose={onClose}
      title="Upraviť monitorovací plán"
      centered
      overlayProps={{
        backgroundOpacity: 0.8,
        blur: 5,
        color: '#0b5942',
      }}
    >
      <Stack gap="md">
        <Group grow>
          <TextInput
            label="PAN číslo lekára"
            type="text"
            ta="left"
            size="md"
            value={planUpdate?.panNumber ?? ''}
            disabled
          />

          <TextInput
            label="Rodné číslo pacienta"
            type="text"
            ta="left"
            size="md"
            value={planUpdate?.personalNumber ?? ''}
            disabled
          />
        </Group>

        <Select
          label="Frekvencia"
          placeholder="Vyberte frekvenciu"
          data={frequency}
          value={planUpdate?.frequency ?? null}
          onChange={(value) =>
            setPlanUpdate({ ...planUpdate, frequency: value })
          }
          size="md"
          searchable
          clearable
          error={errorInputs?.['frequency']}
        />

        <TimePicker
          display={
            planUpdate?.frequency === 'ONE_TIME_DAILY' ||
            planUpdate?.frequency === 'TWO_TIMES_DAILY'
              ? 'block'
              : 'none'
          }
          label="Čas merania 1"
          withDropdown
          ta="left"
          size="md"
          value={planUpdate?.timesOfPlannedMeasurements?.[0] ?? ''}
          onChange={(value) => {
            const times = [...(planUpdate?.timesOfPlannedMeasurements ?? [])];
            times[0] = value;
            setPlanUpdate({ ...planUpdate, timesOfPlannedMeasurements: times });
          }}
          error={errorInputs?.['timesOfPlannedMeasurements']}
        />

        <TimePicker
          display={
            planUpdate?.frequency === 'TWO_TIMES_DAILY' ? 'block' : 'none'
          }
          label="Čas merania 2"
          withDropdown
          ta="left"
          size="md"
          value={planUpdate?.timesOfPlannedMeasurements?.[1] ?? ''}
          onChange={(value) => {
            const times = [...(planUpdate?.timesOfPlannedMeasurements ?? [])];
            times[1] = value;
            setPlanUpdate({ ...planUpdate, timesOfPlannedMeasurements: times });
          }}
          error={errorInputs?.['timesOfPlannedMeasurements']}
        />

        <MultiSelect
          label="Vyberte typy meraní"
          searchable
          clearable
          ta="left"
          size="md"
          data={types.map((type) => ({ value: String(type.id), label: type.typeName, }))}
          value={planUpdate.typeOfMeasurementIds}
          onChange={(value) =>
            setPlanUpdate({
              ...planUpdate,
              typeOfMeasurementIds: value,
            })
          }
          error={errorInputs?.['typeOfMeasurementIds']}
        />

        <Button 
        color="#0b5942" 
        p="xs" size="md" 
        loading={loading}
        onClick={updatePlan}>
          Uložiť plán
        </Button>
      </Stack>
    </Modal>
  );
}
