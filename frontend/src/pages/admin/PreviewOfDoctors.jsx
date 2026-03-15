import { useEffect, useState } from 'react';
import { Group, Stack, Button, Title, Alert, Table, Center, Loader, TextInput } from '@mantine/core';
import { IconSearch } from '@tabler/icons-react';
import AddDoctorModal from '../../components/AddDoctorModal';
import { useDisclosure } from '@mantine/hooks';
import api from '../../configs/api';

export default function PreviewOfDoctors() {
  const [doctors, setDoctors] = useState([]);
  const [opened, { open, close }] = useDisclosure(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [search, setSearch] = useState('');
  const filtered = doctors.filter((item) =>
    [
      item.personalData.firstName,
      item.personalData.lastName,
      item.panNumber,
      item.specialization,
    ].some((value) => value.toLowerCase().includes(search.toLowerCase())),
  );

  useEffect(() => {
    async function fetchDoctors() {
      try {
        const response = await api.get('/doctors');
        setDoctors(response.data);
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
    fetchDoctors();
  }, []);

  if (loading)
    return (
      <Center h="100vh">
        <Loader />
      </Center>
    );

  return (
    <Stack p="md">
      <AddDoctorModal opened={opened} onClose={close} />
      <Group justify="space-between">
        <Title order={2}>Prehľad lekárov</Title>
        <Button onClick={open}>Pridať lekára</Button>
      </Group>

      {doctors.length === 0 ? (
        <Alert bg="yellow">Žiadni lekári nenájdení</Alert>
      ) : (
        <>
          <TextInput
            placeholder="Hľadať..."
            leftSection={<IconSearch size={16} />}
            value={search}
            onChange={(e) => setSearch(e.currentTarget.value)}
          />

          <Table.ScrollContainer minWidth={400} type="native">
            <Table highlightOnHover>
              <Table.Thead bg="primary" c="black">
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
        </>
      )}
    </Stack>
  );
}
