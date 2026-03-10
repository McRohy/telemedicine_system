import { useState, useEffect } from 'react';
import { Modal, Stack, TextInput, Button, NumberInput } from '@mantine/core';
import { notifySuccess, notifyError } from '../configs/notificationHelper';
import api from '../configs/api';

const request = {
  typeName: '',
  units: '',
  minValue: '',
  maxValue: '',
};

export default function EditTypeModal({ opened, onClose, typeId }) {
  const [errorInputs, setErrorInputs] = useState(null);
  const [loading, setLoading] = useState(false);
  const [typeRequest, setTypeRequest] = useState(request);

  useEffect(() => {
    async function fetchTypes() {
      setLoading(true);
      try {
        const response = await api.get(`/measurement-types/${typeId}`);
        setTypeRequest({ ...response.data });
        console.log(response.data);
      } catch (err) {
        if (err.response && err.response.data.message) {
          notifyError(err.response.data.message);
        } else {
          notifyError('Nepodarilo sa načítať dáta');
        }
      } finally {
        setLoading(false);
      }
    }
    fetchTypes();
  }, [typeId]);

  async function editType() {
    setLoading(true);
    try {
      const res = await api.put(`/measurement-types/${typeId}`, typeRequest);
      notifySuccess(
        'Typ merania upravený',
        `${res.data.typeName} s min: ${res.data.minValue} a max: ${res.data.maxValue} jednotkou ${res.data.units} bol úspešne upravený.`,
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
      title="Upraviť typ merania"
      centered
      overlayProps={{
        backgroundOpacity: 0.8,
        blur: 5,
        color: '#0b5942',
      }}
    >
      <Stack gap="md">
        <TextInput
          label="Názov typu merania"
          placeholder="váha, tlak ..."
          type="text"
          ta="left"
          size="md"
          value={typeRequest.typeName}
          disabled
        />

        <TextInput
          label="Jednotky"
          placeholder="kg, cm ..."
          ta="left"
          size="md"
          value={typeRequest.units}
          disabled
        />
        <NumberInput
          label="Minimálna hodnota"
          placeholder="0"
          ta="left"
          size="md"
          min={0}
          value={typeRequest.minValue}
          onChange={(val) => setTypeRequest({ ...typeRequest, minValue: val })}
          onClick={() => setErrorInputs(null)}
          withAsterisk
          error={errorInputs?.['minValue']}
        />

        <NumberInput
          label="Maximálna hodnota"
          placeholder="100"
          ta="left"
          size="md"
          min={0}
          value={typeRequest.maxValue}
          onChange={(val) => setTypeRequest({ ...typeRequest, maxValue: val })}
          onClick={() => setErrorInputs(null)}
          withAsterisk
          error={errorInputs?.['maxValue']}
        />

        <Button
          color="#0b5942"
          p="xs"
          size="md"
          loading={loading}
          onClick={() => editType()}
        >
          Upraviť typ merania
        </Button>
      </Stack>
    </Modal>
  );
}
