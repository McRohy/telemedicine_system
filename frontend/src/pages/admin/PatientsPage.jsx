import { useCallback, useEffect, useState } from 'react';
import { Group, Stack, Button, Title, Alert, Table, TextInput, Loader, Center, Card, Pagination} from '@mantine/core';
import { IconSearch } from '@tabler/icons-react';
import AddPatientModal from '../../components/AddPatientModal';
import { notifyError } from '../../helpers/notificationHelper';
import { useDisclosure, useDebouncedValue } from '@mantine/hooks';
import { getPatients } from '../../api/patientApi';

export default function PreviewOfPatients() {
  const [opened, { open, close }] = useDisclosure(false);
  const [loading, setLoading] = useState(true);

  const [page, setPage] = useState(1);
  const [data, setData] = useState({ content: [], totalPages: 0 });
  const [search, setSearch] = useState('');
  const [debouncedSearch] = useDebouncedValue(search, 300);

  const fetchPatients = useCallback(async () => {
    try {
      const response = await getPatients(page, debouncedSearch);
      setData(response.data);
    } catch (error) {
      notifyError(error);
    } finally {
      setLoading(false);
    }
  }, [page, debouncedSearch]);

  useEffect(() => {
    fetchPatients();
  }, [fetchPatients]);

  if (loading)
    return (
      <Center h="100vh">
        <Loader />
      </Center>
    );

  return (
    <Stack p="md">
      <AddPatientModal opened={opened} onClose={() => { close(); fetchPatients(); }} />
      <Group justify="space-between">
        <Title order={2}>Prehľad pacientov</Title>
        <Button onClick={open}>Pridať pacienta</Button>
      </Group>

      <TextInput
        placeholder="Hľadať podľa priezviska"
        leftSection={<IconSearch size={16} />}
        value={search}
        onChange={(e) => {setSearch(e.currentTarget.value); setPage(1);}}
      />

      {data.content.length === 0 ? (
        <Alert bg="yellow">Žiadni pacienti nenájdení</Alert>
      ) : (
        <Card withBorder shadow="sm" p="md">
          <Table.ScrollContainer minWidth={400} type="native">
            <Table highlightOnHover verticalSpacing="sm">
              <Table.Thead bg="primary" c="black">
                <Table.Tr>
                  <Table.Th>Priezvisko</Table.Th>
                  <Table.Th>Meno</Table.Th>
                  <Table.Th>Rodné číslo</Table.Th>
                  <Table.Th>Ošetrujúci lekár</Table.Th>
                </Table.Tr>
              </Table.Thead>
              <Table.Tbody>
                {data.content.map((p) => (
                  <Table.Tr key={p.personalNumber}>
                    <Table.Td>{p.personalData.lastName}</Table.Td>
                    <Table.Td>{p.personalData.firstName}</Table.Td>
                    <Table.Td>{p.personalNumber}</Table.Td>
                    <Table.Td>{p.doctorPanNumber}</Table.Td>
                  </Table.Tr>
                ))}
              </Table.Tbody>
            </Table>
          </Table.ScrollContainer>

          <Pagination
            justify="flex-end"
            size="sm"
            total={data.totalPages}
            value={page}
            onChange={setPage}
            mt="sm"
          />
        </Card>
      )}
    </Stack>
  );
}
