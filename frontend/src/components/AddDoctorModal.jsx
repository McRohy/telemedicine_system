import { useState } from "react";
import { Modal, Stack, TextInput, Select, Button, Alert } from "@mantine/core";
import { notifySuccess, notifyError } from "../configs/notificationHelper";
import api from "../configs/api";

const specializations = [
  { value: "cardiologist", label: "Kardiológ" },
  { value: "oncologist", label: "Onkológ" },
];

export default function AddDoctorModal({ opened, onClose }) {
    const [errorInputs, setErrorInputs] = useState(null);
    const [doctorRequest, setDoctorRequest] = useState({
      panNumber: '',
      personalData: {
        firstName: '',
        lastName: '',
        email: '',
        role: 'DOCTOR',
      },
      specialization: '',
    });

    async function createDoctor() {
        try {
         const res = await api.post('/doctors', doctorRequest);
        notifySuccess('Lekár pridaný', `${res.data.panNumber} - ${res.data.personalData.firstName} ${res.data.personalData.lastName} (${res.data.specialization}) bol úspešne pridaný.`);
        onClose();

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
      onClose={onClose}
      title="Pridať lekára"
      centered
      overlayProps={{
        backgroundOpacity: 0.8,
        blur: 5,
        color: "#0b5942",
      }}
    >
      <Stack gap="md">
        <TextInput
          label="Meno"
          placeholder="Matej"
          ta="left"
          size="md"
          value={doctorRequest.personalData.firstName}
          onChange={(e) => setDoctorRequest({ ...doctorRequest, personalData: { ...doctorRequest.personalData, firstName: e.target.value } })}
          withAsterisk
          error={errorInputs?.["personalData.firstName"]}
        />
        <TextInput
          label="Priezvisko"
          placeholder="Rohy"
          ta="left"
          size="md"
          value={doctorRequest.personalData.lastName}
          onChange={(e) => setDoctorRequest({ ...doctorRequest, personalData: { ...doctorRequest.personalData, lastName: e.target.value } })}
          withAsterisk
          error={errorInputs?.["personalData.lastName"]}
        />

        <TextInput
          label="PAN číslo"
          placeholder="123456789"
          type="text"
          ta="left"
          size="md"
          value={doctorRequest.panNumber}
          onChange={(e) => setDoctorRequest({ ...doctorRequest, panNumber: e.target.value })}
          withAsterisk
          error={errorInputs?.["panNumber"]}
        />

        <TextInput
          label="Email"
          placeholder="matej.rohy@gmail.sk"
          type="email"
          ta="left"
          size="md"
          value={doctorRequest.personalData.email}
          onChange={(e) => setDoctorRequest({ ...doctorRequest, personalData: { ...doctorRequest.personalData, email: e.target.value } })}
          withAsterisk
          error={errorInputs?.["personalData.email"]}
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
          error={errorInputs?.["specialization"]}
        />

        <Button
        color="#0b5942"
        p="xs"
        size="md"
        onClick={() => createDoctor()}
        >
          Pridať Lekára
        </Button>
      </Stack>
    </Modal>
  );
}
