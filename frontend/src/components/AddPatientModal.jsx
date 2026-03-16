import { useState } from 'react';
import { Modal, Stack, TextInput, Select, Button, Alert } from '@mantine/core';
import { notifySuccess, notifyError } from '../helpers/notificationHelper';
import { GENDERS } from '../helpers/constants';
import { createPatient } from '../api/patientApi';

const request = {
  personalNumber: '',
  personalData: {
    firstName: '',
    lastName: '',
    email: '',
    role: 'PATIENT',
  },
  panNumber: '',
  gender: null,
};

export default function AddPatientModal({ opened, onClose, doctorPanNumber }) {
  const [errorInputs, setErrorInputs] = useState(null);
  const [loading, setLoading] = useState(false);
  const [patientRequest, setPatientRequest] = useState({
    ...request,
    panNumber: doctorPanNumber || '',
  });

  async function handleCreatePatient() {
    setLoading(true);
    try {
      const res = await createPatient(patientRequest);
      notifySuccess(
        'Pacient pridaný',
        `${res.data.personalNumber} - ${res.data.personalData.firstName} ${res.data.personalData.lastName} bol úspešne pridaný lekárovi s PAN: ${res.data.doctorPanNumber}.`,
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
        setPatientRequest({ ...request, panNumber: doctorPanNumber || '' });
        onClose();
      }}
      title="Pridať pacienta"
    >
      <Stack gap="md">
        <TextInput
          label="Rodné číslo"
          placeholder="123456789"
          type="text"
          ta="left"
          size="md"
          value={patientRequest.personalNumber}
          onChange={(e) => setPatientRequest({ ...patientRequest, personalNumber: e.target.value, })}
          withAsterisk
          error={errorInputs?.['personalNumber']}
        />

        <TextInput
          label="Meno"
          placeholder="Matej"
          ta="left"
          size="md"
          value={patientRequest.personalData.firstName}
          onChange={(e) => setPatientRequest({ ...patientRequest, personalData: {...patientRequest.personalData, firstName: e.target.value }})}
          withAsterisk
          error={errorInputs?.['personalData.firstName']}
        />
        <TextInput
          label="Priezvisko"
          placeholder="Rohy"
          ta="left"
          size="md"
          value={patientRequest.personalData.lastName}
          onChange={(e) => setPatientRequest({ ...patientRequest, personalData: {...patientRequest.personalData, lastName: e.target.value }})}
          withAsterisk
          error={errorInputs?.['personalData.lastName']}
        />

        <TextInput
          label="Email"
          placeholder="matej.rohy@gmail.sk"
          type="email"
          ta="left"
          size="md"
          value={patientRequest.personalData.email}
          onChange={(e) => setPatientRequest({ ...patientRequest, personalData: {...patientRequest.personalData, email: e.target.value }})}
          withAsterisk
          error={errorInputs?.['personalData.email']}
        />

        <Select
          label="Pohlavie"
          placeholder="Vyberte pohlavie"
          data={GENDERS}
          size="md"
          searchable
          clearable
          value={patientRequest.gender}
          onChange={(value) => setPatientRequest({ ...patientRequest, gender: value }) }
          withAsterisk
          error={errorInputs?.['gender']}
        />

        <TextInput
          label="PAN číslo lekára"
          placeholder="123456789"
          type="text"
          ta="left"
          size="md"
          value={patientRequest.panNumber}
          onChange={(e) => setPatientRequest({ ...patientRequest, panNumber: e.target.value })}
          withAsterisk
          error={errorInputs?.['panNumber']}
        />

        <Button
          p="xs"
          size="md"
          loading={loading}
          onClick={() => handleCreatePatient()}
        >
          Pridať Pacienta
        </Button>
      </Stack>
    </Modal>
  );
}
