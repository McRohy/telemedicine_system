
import { useEffect, useState } from "react";
import {  Group, Stack, Button, Title, Alert, Table, Center, Loader, TextInput} from "@mantine/core";
import { IconSearch } from '@tabler/icons-react';
import AddDoctorModal from "../../components/AddDoctorModal";
import { useDisclosure } from '@mantine/hooks'

export default function PreviewOfDoctors() {
  const [doctors, setDoctors] = useState([]);
  const [opened, { open, close }] = useDisclosure(false);
  const [loading, setLoading] = useState(true);

  const [search, setSearch] = useState('');
  const filtered = doctors.filter((item) =>
    [item.personalData.firstName, item.personalData.lastName, item.panNumber, item.specialization]
      .some((value) => value.toLowerCase().includes(search.toLowerCase()))
  );
  


  useEffect(() => {
    async function loadDoctors() {
      const res = await fetch("http://localhost:8080/api/doctors");
      const data = await res.json();
      setDoctors(data);
      setLoading(false);
    }
    loadDoctors();
  }, []);

  if (loading) return (
        <Center h="100vh">
                <Loader color="#0b5942" />
        </Center>
      );

  if (doctors.length === 0) {
    return (
      <Stack p="md">
       <AddDoctorModal opened={opened} onClose={close}/> 
        <Group justify="space-between">
          <Title order={2}>Prehľad lekárov</Title>
          <Button
            color='#0b5942'
            p="xs"
            onClick={open}
          >
            Pridať lekára
          </Button>
        </Group>
        <Alert bg="#e5646f61" >
          Žiadni lekári nenájdení
        </Alert>
      </Stack>
    );
  }

  return (
    <Stack p="md">
      <AddDoctorModal opened={opened} onClose={close}/>
      <Group justify="space-between">
        <Title order={2}>Prehľad lekárov</Title>
        <Button
          bg="#0b5942"
          c="white"
          p="xs"
          onClick={open}
        >
          Pridať lekára
        </Button>
      </Group>

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
            <Table.Th>PAN číslo</Table.Th>
            <Table.Th>Meno</Table.Th>
            <Table.Th>Priezvisko</Table.Th>
            <Table.Th>Špecializácia</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {filtered.map((d) => (
            <Table.Tr key={d.panNumber}>
              <Table.Td>{d.panNumber}</Table.Td>
              <Table.Td>{d.personalData.firstName}</Table.Td>
              <Table.Td>{d.personalData.lastName}</Table.Td>
              <Table.Td>{d.specialization}</Table.Td>
            </Table.Tr>
          ))}
        </Table.Tbody>
      </Table>
     </Table.ScrollContainer> 
    </Stack>
  );
}
