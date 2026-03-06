import { useState, useEffect } from "react";
import {
  Modal,
  Stack,
  TextInput,
  Select,
  Button,
  Alert,
  Checkbox,
  Group,
} from "@mantine/core";
import { TimePicker } from "@mantine/dates";

const specializations = [
  { value: "daily", label: "denne" },
  { value: "weekly", label: "týždenne" },
];

export default function PlanModal({opened, onClose, panNumber, personalNumber,}) {
  const [frequency, setFrequency] = useState("");
  const [types, setTypes] = useState([]);
  const [timeOfPlannedMeasurements, setTimeOfPlannedMeasurements] = useState("");
  const [typeOfMeasurementIds, setSelectedTypes] = useState([]);

  const [alert, setAlert] = useState(null);

  useEffect(() => {
    async function loadTypes() {
      const res = await fetch("http://localhost:8080/api/types");
      const data = await res.json();
      setTypes(data);
    }
    loadTypes();
  }, []);

  async function savePlan() {
    const MeasurementPlanRequest = {
      personalNumber: personalNumber,
      panNumber: panNumber,
      frequency,
      timeOfPlannedMeasurements,
      typeOfMeasurementIds,
      
    };

    const res = await fetch("http://localhost:8080/api/plans", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(MeasurementPlanRequest),
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
        <Select
          label="Frekvencia"
          placeholder="Vyberte frekvenciu"
          data={specializations}
          size="md"
          searchable
          clearable
          value={frequency}
          onChange={setFrequency}
        />

        <TimePicker
          label="Čas plánovaných meraní"
          withDropdown
          ta="left"
          size="md"
          value={timeOfPlannedMeasurements}
          onChange={setTimeOfPlannedMeasurements}
        />

        <Checkbox.Group
          label="Vyberte typy meraní"
          value={typeOfMeasurementIds}
          onChange={setSelectedTypes}
        >
          <Group mt="xs">
            {types.map((type) => (
              <Checkbox
                key={type.id}
                value={type.id.toString()}
                label={type.typeName}
              />
            ))}
          </Group>
        </Checkbox.Group>

        <Button
          color="#0b5942"
          p="xs"
          size="md"
          onClick={() => savePlan()}
        >
          Uložiť plán
        </Button>
      </Stack>
    </Modal>
  );
}
