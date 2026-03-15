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
  Pagination,
  Box,
  Card,
} from '@mantine/core';
import { IconSearch } from '@tabler/icons-react';
import AddDoctorModal from '../../components/AddDoctorModal';
import { useDisclosure, useDebouncedValue } from '@mantine/hooks';
import { notifyError } from '../../configs/notificationHelper';
import api from '../../configs/api';

export default function PreviewOfDoctors() {
  const [opened, { open, close }] = useDisclosure(false);
  const [loading, setLoading] = useState(true);

  const [page, setPage] = useState(1);
  const [data, setData] = useState({ content: [], totalPages: 0 });
  const [search, setSearch] = useState('');
  const [debouncedSearch] = useDebouncedValue(search, 300); //not requesting on every key, but after 300ms

  //to prevent infinite loop of useEffect and beacause of need refresh after addDoctor
  const getDoctors = useCallback(async () => {
    try {
      const response = await api({
        url: '/doctors',
        method: 'get',
        params: {
          page: page - 1, //React 1, Spring 0
          size: 10,
          searchLastName: debouncedSearch || undefined,
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
    getDoctors();
  }, [getDoctors]);

  if (loading)
    return (
      <Center h="100vh">
        <Loader />
      </Center>
    );

  return (
    <Stack p="md">
      <AddDoctorModal opened={opened} onClose={() => { close(); getDoctors(); }} />
      <Group justify="space-between">
        <Title order={2}>Prehľad lekárov</Title>
        <Button onClick={open}>Pridať lekára</Button>
      </Group>

      <TextInput
        placeholder="Hľadať podľa priezviska"
        leftSection={<IconSearch size={16} />}
        value={search}
        onChange={(e) => {setSearch(e.currentTarget.value); setPage(1);}}
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
            onChange={setPage}
            mt="sm"
          />
        </Card>
      )}
    </Stack>
  );
}
