import { useEffect, useState } from 'react';
import { Group, Stack, Button, Title, Alert, Table, Center, Loader, TextInput, Card, Pagination } from '@mantine/core';
import { IconSearch } from '@tabler/icons-react';
import AddTypeModal from '../../components/AddTypeModal';
import { useDisclosure, useDebouncedValue } from '@mantine/hooks';
import { notifyError } from '../../helpers/notificationHelper';
import { getMeasurementTypes } from '../../api/measurementTypeApi';

/**
 * Admin page for managing measurement types.
 * Contains a searchable and paginated table of measurement types and a button to open the AddTypeModal.
 */
export default function MeasurementTypesPage() {
  const [isModalOpen, { open: openModal, close: closeModal }] = useDisclosure(false);
  const [loading, setLoading] = useState(true);
  const [refresh, setRefresh] = useState(0);

  const [page, setPage] = useState(1);
  const [data, setData] = useState({ content: [], totalPages: 0 });
  const [search, setSearch] = useState('');
  const [debouncedSearch] = useDebouncedValue(search, 300);

  useEffect(() => {
    async function fetchMeasurementTypes() {
      try {
        const response = await getMeasurementTypes(page, debouncedSearch);
        setData(response.data);
      } catch (error) {
        notifyError(error);
      } finally {
        setLoading(false);
      }
    }
    fetchMeasurementTypes();
  }, [page, debouncedSearch, refresh]);

  if (loading)
    return (
      <Center h="100vh">
        <Loader />
      </Center>
    );

  return (
    <Stack p="md">
      <AddTypeModal
        opened={isModalOpen}
        onClose={() => closeModal()}
        onSuccess={() => setRefresh((r) => r + 1)}
      />
      <Group justify="space-between">
        <Title order={2}>Prehľad typov meraní</Title>
        <Button onClick={() => openModal()}>Pridať typ merania</Button>
      </Group>

      <TextInput
        placeholder="Hľadať podľa názvu typu"
        leftSection={<IconSearch size={16} />}
        value={search}
        onChange={(e) => {
          setSearch(e.currentTarget.value);
          setPage(1);
        }}
      />

      {data.content.length === 0 ? (
        <Alert bg="yellow">Žiadne typy meraní nenájdené</Alert>
      ) : (
        <Card withBorder shadow="sm" p="md">
          <Table.ScrollContainer minWidth={400} type="native">
            <Table highlightOnHover verticalSpacing="sm">
              <Table.Thead bg="primary" c="black">
                <Table.Tr>
                  <Table.Th>Názov typu</Table.Th>

                  <Table.Th>Minimálna hodnota</Table.Th>
                  <Table.Th>Maximálna hodnota</Table.Th>
                  <Table.Th>Jednotky</Table.Th>
                </Table.Tr>
              </Table.Thead>
              <Table.Tbody>
                {data.content.map((t) => (
                  <Table.Tr key={t.id}>
                    <Table.Td>{t.typeName}</Table.Td>
                    <Table.Td>{t.minValue}</Table.Td>
                    <Table.Td>{t.maxValue}</Table.Td>
                    <Table.Td>{t.units}</Table.Td>
                  </Table.Tr>
                ))}
              </Table.Tbody>
            </Table>
          </Table.ScrollContainer>

          <Pagination
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
