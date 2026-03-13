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
  MultiSelect
} from "@mantine/core";
import { TimePicker } from "@mantine/dates";
import { notifySuccess, notifyError } from "../configs/notificationHelper";
import frequency from "../constants/frequency";
import api from "../configs/api";

const request = {
  frequency: null,
  typeOfMeasurementIds: [],
  timesOfPlannedMeasurements: [],
  panNumber: "",
  personalNumber: "",
};


export default function PlanModal({opened, onClose, panNumber, personalNumber,}) {
  const [loading, setLoading] = useState(true);
  const [types, setTypes] = useState([]);
  const [errorInputs, setErrorInputs] = useState(null);
  const [time1, setTime1] = useState('');
  const [time2, setTime2] = useState('');
  const [planRequest, setPlanRequest] = useState({
    ...request,
    panNumber: panNumber,
    personalNumber
  });
  
  

  useEffect(() => {
    async function fetchTypes() {
    try {
      const response = await api.get('/measurement-types');
      setTypes(response.data);
      
    } catch (err) {
      if (err.response && err.response.data.message) {
          //setErrori(err.response.data.message);
      } else {
         // setError('Nepodarilo sa načítať dáta');
  }
    } finally {
      setLoading(false);
    }
  }
  fetchTypes();
  }, []);

  
  async function createPlan() {
    setLoading(true);
    try {
      const timesOfPlannedMeasurements = [];
      if (time1) timesOfPlannedMeasurements.push(time1);
      if (time2) timesOfPlannedMeasurements.push(time2);

      const res = await api.post('/measurement-plans', {
        ...planRequest,
        typeOfMeasurementIds: planRequest.typeOfMeasurementIds.map(Number),
        timesOfPlannedMeasurements,
      });
      notifySuccess(
        'Plán meraní pridaný',
        `Pacient ${res.data.personalNumber} má plán s frekvenciou ${res.data.frequency} a časom plánovaných meraní ${res.data.timesOfPlannedMeasurements}.`,
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
      onClose={onClose}
      title="Vytvoriť monitorovací plán"
      centered
      overlayProps={{
        backgroundOpacity: 0.8,
        blur: 5,
        color: "#0b5942",
      }}
    >
     
      <Stack gap="md">

        <Group grow>
        <TextInput
          label="PAN číslo lekára"
          type="text"
          ta="left"
          size="md"
          value={panNumber}
          disabled
        />

        <TextInput
          label="Rodné číslo pacienta"
          type="text"
          ta="left"
          size="md"
          value={personalNumber}
          disabled
        />
        </Group>

        <Select
          label="Frekvencia"
          placeholder="Vyberte frekvenciu"
          data={frequency}
          size="md"
          searchable
          clearable
          value={planRequest.frequency}
          onChange={(value) => setPlanRequest({ ...planRequest, frequency: value })}
          error={errorInputs?.['frequency']}
        />

       
          <TimePicker
            display={planRequest.frequency === "ONE_TIME_DAILY" || planRequest.frequency === "TWO_TIMES_DAILY" ? "block" : "none"}
            label="Čas merania 1"
            withDropdown
            ta="left"
            size="md"
            value={time1}
            onChange={(value) => setTime1(value)}
            error={errorInputs?.['timesOfPlannedMeasurements']}
          />
       

   
          <TimePicker
            display={planRequest.frequency === "TWO_TIMES_DAILY" ? "block" : "none"}
            label="Čas merania 2"
            withDropdown
            ta="left"
            size="md"
            value={time2}
            onChange={(value) => setTime2(value)}
            error={errorInputs?.['timesOfPlannedMeasurements']}
          />
      

         <MultiSelect
          label="Vyberte typy meraní"
          searchable
          clearable
          ta="left"
          size="md"
          value={planRequest.typeOfMeasurementIds}
          onChange={(value) => setPlanRequest({ ...planRequest, typeOfMeasurementIds: value })}
          data={types.map((type) => ({ value: type.id.toString(), label: type.typeName }))}
          error={errorInputs?.['typeOfMeasurementIds']}
        />

        <Button
          color="#0b5942"
          p="xs"
          size="md"
          loading={loading}
          onClick={() => createPlan()}
        >
          Uložiť plán
        </Button>
      </Stack>
    </Modal>
  );
}
