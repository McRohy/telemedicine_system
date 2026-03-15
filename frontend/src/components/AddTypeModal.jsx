import { useState } from 'react';
import { Modal, Stack, TextInput, Button, NumberInput } from '@mantine/core';
import { notifySuccess, notifyError } from '../configs/notificationHelper';
import api from '../configs/api';

const request = {
  typeName: '',
  units: '',
  minValue: '',
  maxValue: '',
};

export default function AddTypeModal({ opened, onClose }) {
  const [errorInputs, setErrorInputs] = useState(null);
  const [loading, setLoading] = useState(false);
  const [typeRequest, setTypeRequest] = useState(request);

  async function createType() {
    setLoading(true);
    try {
      const res = await api.post('/measurement-types', typeRequest);
      notifySuccess(
        'Typ merania pridaný',
        `${res.data.typeName} s min: ${res.data.minValue} a max: ${res.data.maxValue} jednotkou ${res.data.units} bol úspešne pridaný.`,
      );
      onClose();
      //window.location.reload();
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
      onClose={() => {
        setErrorInputs(null);
        setTypeRequest(request);
        onClose();
      }}
      title="Pridať typ merania"
    >
      <Stack gap="md">
        <TextInput
          label="Názov typu merania"
          placeholder="váha, tlak..."
          type="text"
          ta="left"
          size="md"
          value={typeRequest.typeName}
          onChange={(e) =>
            setTypeRequest({ ...typeRequest, typeName: e.target.value })
          }
          onClick={() => setErrorInputs(null)}
          withAsterisk
          error={errorInputs?.["typeName"]}
        />

        <TextInput
          label="Jednotky"
          placeholder="kg, cm, etc..."
          ta="left"
          size="md"
          value={typeRequest.units}
          onChange={(e) =>
            setTypeRequest({ ...typeRequest, units: e.target.value })
          }
          onClick={() => setErrorInputs(null)}
          withAsterisk
          error={errorInputs?.["units"]}
        />
        <NumberInput
          label="Minimálna hodnota"
          placeholder="0"
          ta="left"
          size="md"
          min={0}
          value={typeRequest.minValue}
          onChange={(val) => 
            setTypeRequest({ ...typeRequest, minValue: val })
          }
          onClick={() => setErrorInputs(null)}
          withAsterisk
          error={errorInputs?.["minValue"]}
        />

        <NumberInput
          label="Maximálna hodnota"
          placeholder="100"
          ta="left"
          size="md"
          min={0}
          value={typeRequest.maxValue}
          onChange={(val) => setTypeRequest({ ...typeRequest, maxValue: val })
          }
          onClick={() => setErrorInputs(null)}
          withAsterisk
          error={errorInputs?.["maxValue"]}
        />

        <Button
          p="xs"
          size="md"
          loading={loading}
          onClick={() => createType()}
        >
          Pridať typ merania
        </Button>
      </Stack>
    </Modal>
  );
}
