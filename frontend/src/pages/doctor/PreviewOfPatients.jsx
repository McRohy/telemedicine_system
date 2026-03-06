import { useEffect, useState } from "react";
import {  Group, Stack, Button, Title, Alert, Table, Loader, Center} from "@mantine/core";
import AddPatientModal from "../../components/AddPatientModal";
import { useDisclosure } from '@mantine/hooks'
import { useNavigate } from "react-router-dom";

export default function PreviewOfPatients() {
  const testDoctorPanNumber = "1243095387543123";
  const navigate = useNavigate();
  const [patients, setPatients] = useState([]);
  const [opened, { open, close }] = useDisclosure(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadPatients() {
      const res = await fetch(`http://localhost:8080/api/patients?panNumber=${testDoctorPanNumber}`);
      const data = await res.json();
      setPatients(data);
      setLoading(false);
    }
    loadPatients();
  }, []);

  if (loading) return (
      <Center h="100vh">
        <Loader color="#0b5942" />
      </Center>
    );

  if (patients.length === 0) {
    return (
      <Stack p="md">
      <AddPatientModal opened={opened} onClose={close} doctorPanNumber={testDoctorPanNumber} />
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
      <AddPatientModal opened={opened} onClose={close} doctorPanNumber={testDoctorPanNumber} />
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
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {patients.map((d) => (
            <Table.Tr
              key={d.personalNumber}
              onClick={() => navigate(`/doctor/patients/${d.personalNumber}`)}
            >
              <Table.Td>{d.personalNumber}</Table.Td>
              <Table.Td>{d.personalData.firstName}</Table.Td>
              <Table.Td>{d.personalData.lastName}</Table.Td>
            </Table.Tr>
          ))}
        </Table.Tbody>
      </Table>
     </Table.ScrollContainer> 
    </Stack>
  );
}
