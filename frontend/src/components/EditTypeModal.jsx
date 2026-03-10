import { useState, useEffect } from "react";
import { Modal, Stack, TextInput, Button, NumberInput, } from "@mantine/core";
import { notifyLoading, notifyUpdate } from "../configs/notificationHelper";
import { notifyError } from "../configs/notificationHelper";
import api from "../configs/api";

export default function EditTypeModal({ opened, onClose, typeId }) {
    const [errorInputs, setErrorInputs] = useState(null);
    const [typeRequest, setTypeRequest] = useState(
        {
            typeName: '',
            units: '',
            minValue: '',
            maxValue: '',
        }
    );

    useEffect(() => {
    async function fetchTypes() {
    try {
      const response = await api.get(`/types/${typeId}`);
      setTypeRequest({ ...response.data });
      console.log(response.data);
      
    } catch (err) {
      if (err.response && err.response.data.message) {
         notifyError(err.response.data.message);
      } else {
         notifyError('Nepodarilo sa načítať dáta');
      }
    }
  }
  fetchTypes();
  }, [typeId]);


    async function editType() {
        notifyLoading('edit-type', 'Typ merania', 'úprava typu merania...');
        try {
          const res = await api.put('/types', typeRequest);
          notifyUpdate('edit-type', 'Typ merania upravený', `${res.data.typeName} s min: ${res.data.minValue} a max: ${res.data.maxValue} jednotkou ${res.data.units} bol úspešne upravený.`);
          onClose();
          window.location.reload();
       } catch (err) {
          console.log(err.response);
         const status = err.response?.status;

        if (status === 400) {
          notifyUpdate('edit-type', 'Chyba', 'Skontrolujte zadané údaje.', 'red');
          setErrorInputs(err.response.data.fieldErrors);
        } else {
          notifyUpdate('edit-type', status, err.response?.data?.message, 'red');
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
      title="Upraviť typ merania"
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
          disabled
        />

        <TextInput
          label="Jednotky"
          placeholder="kg, cm, etc..."
          ta="left"
          size="md"
          value={typeRequest.units}
          disabled
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
        onClick={() => editType()}
        >
          Upraviť typ merania
        </Button>
      </Stack>
    </Modal>
  );
}
