import { useState } from 'react';
import { Modal, Stack, TextInput, Select, Button } from '@mantine/core';
import { notifySuccess, notifyError } from '../helpers/notificationHelper';
import { GENDERS } from '../helpers/constants';
import { createPatient } from '../api/patientApi';
import { useForm } from '@mantine/form';

/** 
 * Modal for creating a new patient. 
 * Contains a form with patient details and a submit button to add patient.
 */
export default function AddPatientModal({opened, onClose, onSuccess, doctorPanNumber}) {
  const [loading, setLoading] = useState(false);
  const formRequest = useForm({
    initialValues: {
      personalNumber: '',
      personalData: {
        firstName: '',
        lastName: '',
        email: '',
      },
      panNumber: doctorPanNumber || '',
      gender: null,
    },
    validate: {
      personalNumber: (value) => (value ? null : 'Povinné pole'),
      'personalData.firstName': (value) => (value ? null : 'Povinné pole'),
      'personalData.lastName': (value) => (value ? null : 'Povinné pole'),
      'personalData.email': (value) => (value ? null : 'Povinné pole'),
      panNumber: (value) => (value ? null : 'Povinné pole'),
      gender: (value) => (value ? null : 'Povinné pole'),
    },
  });

  async function handleCreatePatient() {
    setLoading(true);
    try {
      const res = await createPatient(formRequest.values);
      notifySuccess(
        'Pacient pridaný',
        `${res.data.personalNumber} - ${res.data.personalData.firstName} ${res.data.personalData.lastName} bol úspešne pridaný lekárovi s PAN: ${res.data.doctorPanNumber}.`,
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
      title="Pridať pacienta"
    >
      <form onSubmit={formRequest.onSubmit(handleCreatePatient)}>
        <Stack gap="md">
          <TextInput
            label="Rodné číslo"
            placeholder="123456789"
            type="text"
            ta="left"
            size="md"
            withAsterisk
            {...formRequest.getInputProps('personalNumber')}
          />

          <TextInput
            label="Meno"
            placeholder="Matej"
            ta="left"
            size="md"
            withAsterisk
            {...formRequest.getInputProps('personalData.firstName')}
          />
          <TextInput
            label="Priezvisko"
            placeholder="Rohy"
            ta="left"
            size="md"
            withAsterisk
            {...formRequest.getInputProps('personalData.lastName')}
          />

          <TextInput
            label="Email"
            placeholder="matej.rohy@gmail.sk"
            type="email"
            ta="left"
            size="md"
            withAsterisk
            {...formRequest.getInputProps('personalData.email')}
          />

          <Select
            label="Pohlavie"
            placeholder="Vyberte pohlavie"
            data={GENDERS}
            size="md"
            searchable
            clearable
            withAsterisk
            {...formRequest.getInputProps('gender')}
          />

         {!doctorPanNumber && (
          <TextInput
            label="PAN číslo lekára"
            placeholder="123456789"
            type="text"
            ta="left"
            size="md"
            withAsterisk
            {...formRequest.getInputProps('panNumber')}
          />
          )}

          <Button type="submit" p="xs" size="md" loading={loading}>
            Pridať Pacienta
          </Button>
        </Stack>
      </form>
    </Modal>
  );
}
