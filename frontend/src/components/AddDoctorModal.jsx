import { useState } from 'react';
import { Modal, Stack, TextInput, Select, Button } from '@mantine/core';
import { notifySuccess, notifyError } from '../helpers/notificationHelper';
import { SPECIALIZATIONS } from '../helpers/constants';
import { createDoctor } from '../api/doctorApi';
import { useForm } from '@mantine/form';

/** 
 * Modal for creating a new doctor. 
 * Contains a form with doctor details and a submit button to add doctor.
 */
export default function AddDoctorModal({ opened, onClose, onSuccess }) {
  const [loading, setLoading] = useState(false);
  const formRequest = useForm({
    initialValues: {
      panNumber: '',
      personalData: {
        firstName: '',
        lastName: '',
        email: '',
      },
      specialization: null,
    },
    validate: {
      panNumber: (value) => (value ? null : 'Povinné pole'),
      'personalData.firstName': (value) => (value ? null : 'Povinné pole'),
      'personalData.lastName': (value) => (value ? null : 'Povinné pole'),
      'personalData.email': (value) => (value ? null : 'Povinné pole'),
      specialization: (value) => (value ? null : 'Povinné pole'),
    },
  });

  async function handleCreateDoctor() {
    setLoading(true);
    try {
      const res = await createDoctor(formRequest.values);
      notifySuccess(
        'Lekár pridaný',
        `${res.data.panNumber} - ${res.data.personalData.firstName} ${res.data.personalData.lastName} (${res.data.specialization}) bol úspešne pridaný.`,
      );
      formRequest.reset();
      onClose();
      onSuccess();
    } catch (error) {
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
      title="Pridať lekára"
    >
      <form onSubmit={formRequest.onSubmit(handleCreateDoctor)}>
        <Stack gap="md">
          <TextInput
            label="Meno"
            placeholder="Matej"
            ta="left"
            size="md"
            withAsterisk
            {...formRequest.getInputProps('personalData.firstName')} // value, onChange, error in one line
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
            label="PAN číslo"
            placeholder="123456789"
            type="text"
            ta="left"
            size="md"
            withAsterisk
            {...formRequest.getInputProps('panNumber')}
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
            label="Špecializácia"
            placeholder="Vyberte špecializáciu"
            data={SPECIALIZATIONS}
            size="md"
            searchable
            clearable
            withAsterisk
            {...formRequest.getInputProps('specialization')}
          />

          <Button type="submit" p="xs" size="md" loading={loading}>
            Pridať Lekára
          </Button>
        </Stack>
      </form>
    </Modal>
  );
}
