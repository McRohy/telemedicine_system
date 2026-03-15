import { useState } from 'react';
import { Modal, Stack, TextInput, Select, Button } from '@mantine/core';
import { notifySuccess, notifyError } from '../configs/notificationHelper';
import specializations from '../constants/specializations';
import api from '../configs/api';

const request = {
  panNumber: '',
  personalData: {
    firstName: '',
    lastName: '',
    email: '',
    role: 'DOCTOR',
  },
  specialization: null,
};

export default function AddDoctorModal({ opened, onClose }) {
  const [errorInputs, setErrorInputs] = useState(null);
  const [loading, setLoading] = useState(false);
  const [doctorRequest, setDoctorRequest] = useState(request);

  async function createDoctor() {
    setLoading(true);
    try {
      const res = await api.post('/doctors', doctorRequest);
      notifySuccess(
        'Lekár pridaný',
        `${res.data.panNumber} - ${res.data.personalData.firstName} ${res.data.personalData.lastName} (${res.data.specialization}) bol úspešne pridaný.`,
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
        setDoctorRequest(request);
        onClose();
      }}
      title="Pridať lekára"
    >
      <Stack gap="md">
        <TextInput
          label="Meno"
          placeholder="Matej"
          ta="left"
          size="md"
          value={doctorRequest.personalData.firstName}
          onChange={(e) => setDoctorRequest({ ...doctorRequest, personalData: {  ...doctorRequest.personalData, firstName: e.target.value, }, }) }
          withAsterisk
          error={errorInputs?.['personalData.firstName']}
        />
        <TextInput
          label="Priezvisko"
          placeholder="Rohy"
          ta="left"
          size="md"
          value={doctorRequest.personalData.lastName}
          onChange={(e) => setDoctorRequest({ ...doctorRequest, personalData: {  ...doctorRequest.personalData, lastName: e.target.value, }, }) }
          withAsterisk
          error={errorInputs?.['personalData.lastName']}
        />

        <TextInput
          label="PAN číslo"
          placeholder="123456789"
          type="text"
          ta="left"
          size="md"
          value={doctorRequest.panNumber}
          onChange={(e) => setDoctorRequest({ ...doctorRequest, panNumber: e.target.value }) }
          withAsterisk
          error={errorInputs?.['panNumber']}
        />

        <TextInput
          label="Email"
          placeholder="matej.rohy@gmail.sk"
          type="email"
          ta="left"
          size="md"
          value={doctorRequest.personalData.email}
          onChange={(e) => setDoctorRequest({ ...doctorRequest, personalData: { ...doctorRequest.personalData, email: e.target.value } }) }
          withAsterisk
          error={errorInputs?.['personalData.email']}
        />

        <Select
          label="Špecializácia"
          placeholder="Vyberte špecializáciu"
          data={specializations}
          size="md"
          searchable
          clearable
          value={doctorRequest.specialization}
          onChange={(value) => setDoctorRequest({ ...doctorRequest, specialization: value })}
          withAsterisk
          error={errorInputs?.['specialization']}
        />

        <Button
          p="xs"
          size="md"
          loading={loading}
          onClick={() => createDoctor()}
        >
          Pridať Lekára
        </Button>
      </Stack>
    </Modal>
  );
}
