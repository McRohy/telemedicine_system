import { useEffect, useState } from "react";
import {  Group, Stack, Button, Title, Alert, Table, Loader, Center, TextInput} from "@mantine/core";
import AddPatientModal from "../../components/AddPatientModal";
import { useDisclosure } from '@mantine/hooks'
import { useNavigate } from "react-router-dom";
import { IconSearch } from '@tabler/icons-react';
import { useAuth } from "../../context/AuthContext";
import api from "../../configs/api";

export default function PreviewOfPatients() {
  const {user} = useAuth();
  const actualDoctorPanNumber = user?.identificationNumber;
  const navigate = useNavigate();
  const [patients, setPatients] = useState([]);
  const [opened, { open, close }] = useDisclosure(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [search, setSearch] = useState('');
    const filtered = patients.filter((item) =>
      [item.personalData.firstName, item.personalData.lastName, item.personalNumber, item.doctorPanNumber]
        .some((value) => value.toLowerCase().includes(search.toLowerCase()))
    );

  useEffect(() => {
    async function fetchPatients() {
      try {
        const response = await api.get(`/patients?panNumber=${actualDoctorPanNumber}`);
        setPatients(response.data);
      } catch (err) {
        if (err.response && err.response.data.message) {
          setError(err.response.data.message);
        } else {
          setError('Nepodarilo sa načítať dáta');
        }
      } finally {
        setLoading(false);
      }
    }
    fetchPatients();
  }, []);

  if (loading) return (
      <Center h="100vh">
        <Loader color="#0b5942" />
      </Center>
    );

  if (patients.length === 0) {
    return (
      <Stack p="md">
      <AddPatientModal opened={opened} onClose={close} doctorPanNumber={actualDoctorPanNumber} />
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
        <Alert bg="#ffee7d61" >
          Žiadni pacienti nenájdení
        </Alert>
      </Stack>
    );
  }

  return (
    <Stack p="md">
      <AddPatientModal opened={opened} onClose={close} doctorPanNumber={actualDoctorPanNumber} />
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

      <Alert color="red" hidden={!error}>
        {error}
      </Alert>

     <TextInput
        placeholder="Hľadať..."
        leftSection={<IconSearch size={16} />}
        value={search}
        onChange={(e) => setSearch(e.currentTarget.value)}
      />

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
          {filtered.map((d) => (
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
