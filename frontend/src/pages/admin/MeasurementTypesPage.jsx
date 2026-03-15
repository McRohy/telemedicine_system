import { useCallback, useEffect, useState } from 'react';
import {
  Group,
  Stack,
  Button,
  Title,
  Alert,
  Table,
  Center,
  Loader,
  TextInput,
  Text,
  Card,
  Pagination,
} from '@mantine/core';
import { IconSearch, IconEdit } from '@tabler/icons-react';
import AddTypeModal from '../../components/AddTypeModal';
import { useDisclosure, useDebouncedValue } from '@mantine/hooks';
import { notifyError } from '../../configs/notificationHelper';
import api from '../../configs/api';

export default function MeasurementTypesPage() {
  const [opened, { open, close }] = useDisclosure(false);
  const [loading, setLoading] = useState(true);

  const [page, setPage] = useState(1);
  const [data, setData] = useState({ content: [], totalPages: 0 });
  const [search, setSearch] = useState('');
  const [debouncedSearch] = useDebouncedValue(search, 300);

  const getTypes = useCallback(async () => {
    try {
      const response = await api({
        url: '/measurement-types',
        method: 'get',
        params: {
          page: page - 1, //React 1, Spring 0
          size: 10,
          search: debouncedSearch || undefined,
        },
      });
      setData(response.data);
    } catch (error) {
      notifyError(error);
    } finally {
      setLoading(false);
    }
  }, [page, debouncedSearch]);

  useEffect(() => {
    getTypes();
  }, [getTypes]);

  if (loading)
    return (
      <Center h="100vh">
        <Loader />
      </Center>
    );

  return (
    <Stack p="md">
      <AddTypeModal
        opened={opened}
        onClose={() => {
          close();
          getTypes();
        }}
      />
      <Group justify="space-between">
        <Title order={2}>Prehľad typov meraní</Title>
        <Button onClick={open}>Pridať typ merania</Button>
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
