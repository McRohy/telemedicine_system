import { useEffect, useState } from 'react';
import { Group, Stack, Button, Title, Alert, Table, Center, Loader, TextInput, Pagination, Card } from '@mantine/core';
import { IconSearch } from '@tabler/icons-react';
import AddDoctorModal from '../../components/AddDoctorModal';
import { useDisclosure, useDebouncedValue } from '@mantine/hooks';
import { notifyError } from '../../helpers/notificationHelper';
import { getDoctors } from '../../api/doctorApi';

export default function DoctorsPage() {
  const [isModalOpen, { open: openModal, close: closeModal }] = useDisclosure(false);
  const [loading, setLoading] = useState(true);
  const [refresh, setRefresh] = useState(0); // hack to trigger refresh after adding doctor 

  const [page, setPage] = useState(1);
  const [data, setData] = useState({ content: [], totalPages: 0 });
  const [search, setSearch] = useState('');
  const [debouncedSearch] = useDebouncedValue(search, 300); //not requesting on every key, but after 300ms

  useEffect(() => {
    async function fetchDoctors() {
      try {
        const response = await getDoctors(page, debouncedSearch);
        setData(response.data);
      } catch (error) {
        notifyError(error);
      } finally {
        setLoading(false);
      }
    }
    fetchDoctors();
  }, [page, debouncedSearch, refresh]);

  if (loading)
    return (
      <Center h="100vh">
        <Loader />
      </Center>
    );

  return (
    <Stack p="md">
      <AddDoctorModal
        opened={isModalOpen}
        onClose={() => closeModal()}
        onSuccess={() => setRefresh((r) => r + 1)}
      />
      <Group justify="space-between">
        <Title order={2}>Prehľad lekárov</Title>
        <Button onClick={() => openModal()}>Pridať lekára</Button>
      </Group>

      <TextInput
        placeholder="Hľadať podľa priezviska"
        leftSection={<IconSearch size={16} />}
        value={search}
        onChange={(e) => {
          setSearch(e.currentTarget.value);
          setPage(1);
        }}
      />

      {data.content.length === 0 ? (
        <Alert bg="yellow">Žiadni lekári nenájdení</Alert>
      ) : (
        <Card withBorder shadow="sm" p="md">
          <Table.ScrollContainer minWidth={400} type="native">
            <Table highlightOnHover verticalSpacing="sm">
              <Table.Thead bg="primary" c="black">
                <Table.Tr>
                  <Table.Th>Priezvisko</Table.Th>
                  <Table.Th>Meno</Table.Th>
                  <Table.Th>Špecializácia</Table.Th>
                  <Table.Th>PAN číslo</Table.Th>
                </Table.Tr>
              </Table.Thead>
              <Table.Tbody>
                {data.content.map((d) => (
                  <Table.Tr key={d.panNumber}>
                    <Table.Td>{d.personalData.lastName}</Table.Td>
                    <Table.Td>{d.personalData.firstName}</Table.Td>
                    <Table.Td>{d.specialization}</Table.Td>
                    <Table.Td>{d.panNumber}</Table.Td>
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
            onChange={(value) => setPage(value)}
            mt="sm"
          />
        </Card>
      )}
    </Stack>
  );
}
