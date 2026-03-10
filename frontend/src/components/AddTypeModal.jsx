import { useState } from "react";
import { Modal, Stack, TextInput, Select, Button, NumberInput, } from "@mantine/core";
import { notifyLoading, notifyUpdate } from "../configs/notificationHelper";
import api from "../configs/api";

export default function AddTypeModal({ opened, onClose }) {
    const [errorInputs, setErrorInputs] = useState(null);
    const [typeRequest, setTypeRequest] = useState({
      typeName: '',
      units: '',
      minValue: '',
      maxValue: '',
    });


    async function createType() {
        notifyLoading('create-type', 'Typ merania', 'vytvám nový typ merania...');
        try {
          const res = await api.post('/types', typeRequest);
          notifyUpdate('create-type', 'Typ merania pridaný', `${res.data.typeName} s min: ${res.data.minValue} a max: ${res.data.maxValue} jednotkou ${res.data.units} bol úspešne pridaný.`);
          onClose();
          window.location.reload();
       } catch (err) {
          console.log(err.response);
         const status = err.response?.status;

        if (status === 400) {
          notifyUpdate('create-type', 'Chyba', 'Skontrolujte zadané údaje.', 'red');
          setErrorInputs(err.response.data.fieldErrors);
        } else {
          notifyUpdate('create-type', status, err.response?.data?.message, 'red');
        }
     }
   }

  return (
    <Modal
      opened={opened}
      onClose={() => {
        setErrorInputs(null);
        setTypeRequest({
          typeName: '',
          units: '',
          minValue: '',
          maxValue: '',
        });
        onClose();
      }}
      title="Pridať typ merania"
      centered
      overlayProps={{
        backgroundOpacity: 0.8,
        blur: 5,
        color: "#0b5942",
      }}
    >
      <Stack gap="md">

        <TextInput
          label="Názov typu merania"
          placeholder="váha, tlak..."
          type="text"
          ta="left"
          size="md"
          value={typeRequest.typeName}
          onChange={(e) => setTypeRequest({ ...typeRequest, typeName: e.target.value })}
          onClick={() => setErrorInputs(null)}
          withAsterisk
          error={errorInputs?.["typeName"]}
        />

        <TextInput
          label="Jednotky"
          placeholder="kg, cm, etc..."
          ta="left"
          size="md"
          value={typeRequest.units}
          onChange={(e) => setTypeRequest({ ...typeRequest, units: e.target.value })}
          onClick={() => setErrorInputs(null)}
          withAsterisk
          error={errorInputs?.["units"]}
        />
        <NumberInput
          label="Minimálna hodnota"
          placeholder="0"
          ta="left"
          size="md"
          min={0}
          value={typeRequest.minValue}
          onChange={(val) => setTypeRequest({ ...typeRequest, minValue: val })}
          onClick={() => setErrorInputs(null)}
          withAsterisk
          error={errorInputs?.["minValue"]}
        />

        <NumberInput
          label="Maximálna hodnota"
          placeholder="100"
          ta="left"
          size="md"
          min={0}
          value={typeRequest.maxValue}
          onChange={(val) => setTypeRequest({ ...typeRequest, maxValue: val })}
          onClick={() => setErrorInputs(null)}
          withAsterisk
          error={errorInputs?.["maxValue"]}
        />

        <Button
        color="#0b5942"
        p="xs"
        size="md"
        onClick={() => createType()}
        >
          Pridať typ merania
        </Button>
      </Stack>
    </Modal>
  );
}
