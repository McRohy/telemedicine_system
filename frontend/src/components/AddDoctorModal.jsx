import { useState } from "react";
import { Modal, Stack, TextInput, Select, Button, Alert } from "@mantine/core";

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

        const res = await fetch('http://localhost:8080/api/doctors', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(doctorRequest),
        });

        if (res.ok) {
            onClose();
        } else {
            const error = await res.json();
            setAlert(error.message);
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
