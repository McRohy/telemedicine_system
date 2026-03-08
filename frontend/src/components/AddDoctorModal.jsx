import { useState } from "react";
import { Modal, Stack, TextInput, Select, Button, Alert } from "@mantine/core";
import { notifications } from '@mantine/notifications';
import api from "../configs/api";

const specializations = [
  { value: "cardiologist", label: "Kardiológ" },
  { value: "oncologist", label: "Onkológ" },
];

export default function AddDoctorModal({ opened, onClose }) {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [panNumber, setPanNumber] = useState('');
    const [specialization, setSpecialization] = useState('');
    const [email, setEmail] = useState('');
    const [alert, setAlert] = useState(null);

    async function createDoctor() {
        const personalData = {
            firstName,
            lastName,
            email,
            role: 'DOCTOR',
        };

        const doctorRequest = {
            panNumber,
            personalData,
            specialization,
        };

        try {
         const res =await api.post('/doctors', doctorRequest);
        notifications.show({
          title: 'Lekár pridaný',
          message: `${res.data.panNumber} - ${res.data.personalData.firstName} ${res.data.personalData.lastName} (${res.data.specialization}) bol úspešne pridaný.`,
          position: 'top-right',
          color: "#0b5942",
        });
        onClose();
        
       } catch (err) {
       if (err.response && err.response.data.message) {
          setAlert(err.response.data.message);
           } else {
          setAlert('Nepodarilo sa načítať dáta');
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
      <Alert color="red" hidden={!alert} mb="md">
        {alert}
      </Alert>
      <Stack gap="md">
        <TextInput
          label="Meno"
          placeholder="Matej"
          ta="left"
          size="md"
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
        />
        <TextInput
          label="Priezvisko"
          placeholder="Rohy"
          ta="left"
          size="md"
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
        />

        <TextInput
          label="PAN číslo"
          placeholder="123456789"
          type="text"
          ta="left"
          size="md"
          value={panNumber}
          onChange={(e) => setPanNumber(e.target.value)}
        />

        <TextInput
          label="Email"
          placeholder="matej.rohy@gmail.sk"
          type="email"
          ta="left"
          size="md"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <Select
          label="Špecializácia"
          placeholder="Vyberte špecializáciu"
          data={specializations}
          size="md"
          searchable
          clearable
          value={specialization}
          onChange={setSpecialization}
        />

        <Button
        color="#0b5942"
        p="xs"
        size="md"
        onClick={() => createDoctor()}
        disabled={!firstName || !lastName || !panNumber || !email || !specialization}
        >
          Pridať Lekára
        </Button>
      </Stack>
    </Modal>
  );
}
