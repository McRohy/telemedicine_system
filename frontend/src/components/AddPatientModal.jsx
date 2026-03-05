import { useState } from "react";
import { Modal, Stack, TextInput, Select, Button, Alert } from "@mantine/core";


export default function AddPatientModal({ opened, onClose, doctorPanNumber }) {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [panNumber, setPanNumber] = useState(doctorPanNumber || '');
    const [personalNumber, setPersonalNumber] = useState('');
    const [email, setEmail] = useState('');
    const [alert, setAlert] = useState(null);

    async function createPatient() {
        const personalData = {
            firstName,
            lastName,
            email,
            role: 'PATIENT',
        };

        const patientRequest = {
            personalNumber,
            personalData,
            panNumber,
        };

        const res = await fetch('http://localhost:8080/api/patients', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(patientRequest),
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
      title="Pridať pacienta"
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
          label="Rodné číslo"
          placeholder="123456789"
          type="text"
          ta="left"
          size="md"
          value={personalNumber}
          onChange={(e) => setPersonalNumber(e.target.value)}
        />

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
          label="Email"
          placeholder="matej.rohy@gmail.sk"
          type="email"
          ta="left"
          size="md"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <TextInput
          label="PAN číslo lekára"
          placeholder="123456789"
          type="text"
          ta="left"
          size="md"
          value={panNumber}
          onChange={(e) => setPanNumber(e.target.value)}
        />

        <Button
        color="#0b5942"
        p="xs"
        size="md"
        onClick={() => createPatient()}
        disabled={!firstName || !lastName || !panNumber || !email || !personalNumber}
        >
          Pridať Pacienta
        </Button>
      </Stack>
    </Modal>
  );
}
