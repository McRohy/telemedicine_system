import { useEffect, useState } from "react";
import {  Group, Stack, Button, Title, Alert, Table } from "@mantine/core";
import AddPatientModal from "../../components/AddPatientModal";
import { useDisclosure } from '@mantine/hooks'

export default function PreviewOfPatients() {
  const [patients, setPatients] = useState([]);
  const [opened, { open, close }] = useDisclosure(false);

  useEffect(() => {
    async function loadPatients() {
      const res = await fetch("http://localhost:8080/api/patients?panNumber=9703990654280621");
      const data = await res.json();
      setPatients(data);
    }
    loadPatients();
  }, []);

  if (patients.length === 0) {
    return (
      <Stack p="md">
      <AddPatientModal opened={opened} onClose={close} doctorPanNumber="9703990654280621"/>
        <Group justify="space-between">
          <Title order={2}>Prehľad pacientov</Title>
          <Button
            color="#0b5942"
            p="xs"
            onClick={open}
          >
            Pridať pacienta
          </Button>
        </Group>
        <Alert bg="#e5646f61" >
          Žiadni pacienti nenájdení
        </Alert>
      </Stack>
    );
  }

  return (
    <Stack p="md">
      <AddPatientModal opened={opened} onClose={close} doctorPanNumber="9703990654280621" />
      <Group justify="space-between">
        <Title order={2}>Prehľad pacientov</Title>
        <Button
          bg="#0b5942"
          c="white"
          p="xs"
          onClick={open}
        >
          Pridať pacienta
        </Button>
      </Group>

     <Table.ScrollContainer minWidth={400} type="native">
      <Table highlightOnHover>
        <Table.Thead bg="#0b5942" c="white">
          <Table.Tr>
            <Table.Th>Rodné číslo</Table.Th>
            <Table.Th>Meno</Table.Th>
            <Table.Th>Priezvisko</Table.Th>
            <Table.Th>Ošetrujúci lekár</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {patients.map((d) => (
            <Table.Tr key={d.personalNumber}>
              <Table.Td>{d.personalNumber}</Table.Td>
              <Table.Td>{d.personalData.firstName}</Table.Td>
              <Table.Td>{d.personalData.lastName}</Table.Td>
              <Table.Td>{d.doctorPanNumber}</Table.Td>
            </Table.Tr>
          ))}
        </Table.Tbody>
      </Table>
     </Table.ScrollContainer> 
    </Stack>
  );
}
