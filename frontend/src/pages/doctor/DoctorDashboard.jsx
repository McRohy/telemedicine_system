import { useEffect, useState } from 'react';
import { Group, Stack, Button, Title, Table, Loader, Center, TextInput, Card, Pagination, Anchor, Alert } from '@mantine/core';
import AddPatientModal from '../../components/AddPatientModal';
import { useDisclosure, useDebouncedValue } from '@mantine/hooks';
import { Link } from 'react-router-dom';
import { IconSearch, IconArrowUpRight } from '@tabler/icons-react';
import { useAuth } from '../../context/AuthContext';
import { notifyError } from '../../helpers/notificationHelper';
import { getPatientsByPanNumber } from '../../api/patientApi';
import { GENDERS } from '../../helpers/constants';

/**
 * Doctor dashboard for managing patients.
 * Contains a searchable and paginated table of patients and a button to open the AddPatientModal.
 */
export default function DoctorDashboard() {
  const { user } = useAuth();
  const actualDoctorPanNumber = user?.identificationNumber;

  const [isModalOpen, { open: openModal, close: closeModal }] = useDisclosure(false);
  const [loading, setLoading] = useState(true);
  const [refresh, setRefresh] = useState(0);

  const [page, setPage] = useState(1);
  const [data, setData] = useState({ content: [], totalPages: 0 });
  const [search, setSearch] = useState('');
  const [debouncedSearch] = useDebouncedValue(search, 300);

  useEffect(() => {
    async function fetchPatients() {
      try {
        const response = await getPatientsByPanNumber( actualDoctorPanNumber, page, debouncedSearch );
        setData(response.data);
      } catch (error) {
        notifyError(error);
      } finally {
        setLoading(false);
      }
    }
    fetchPatients();
  }, [actualDoctorPanNumber, page, debouncedSearch, refresh]);

  if (loading)
    return (
      <Center h="100vh">
        <Loader />
      </Center>
    );

  return (
    <Stack p="md">
      <AddPatientModal
        opened={isModalOpen}
        onClose={() => closeModal()}
        onSuccess={() => setRefresh((r) => r + 1)}
        doctorPanNumber={actualDoctorPanNumber}
      />
      <Group justify="space-between">
        <Title order={2}>Prehľad pacientov</Title>
        <Button p="xs" onClick={() => openModal()}>
          Pridať pacienta
        </Button>
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
        <Alert bg="yellow">Žiadni pacienti nenájdení</Alert>
      ) : (
        <Card withBorder shadow="sm" p="md">
          <Table.ScrollContainer minWidth={400} type="native">
            <Table highlightOnHover>
              <Table.Thead bg="primary">
                <Table.Tr>
                  <Table.Th>Priezvisko</Table.Th>
                  <Table.Th>Meno</Table.Th>
                  <Table.Th>Pohlavie</Table.Th>
                  <Table.Th>Rodné číslo</Table.Th>
                  <Table.Th><IconArrowUpRight size={16} color="black" /></Table.Th>
                </Table.Tr>
              </Table.Thead>
              <Table.Tbody>
                {data.content.map((d) => (
                  <Table.Tr key={d.personalNumber}>
                    <Table.Td>{d.personalData.lastName}</Table.Td>
                    <Table.Td>{d.personalData.firstName}</Table.Td>
                    <Table.Td>{GENDERS.find((g) => g.value === d?.gender)?.label}</Table.Td>
                    <Table.Td>{d.personalNumber}</Table.Td>
                    <Table.Td>
                      <Anchor
                        component={Link}
                        to={`/doctor/patients/${encodeURIComponent(btoa(d.personalNumber))}`} //masking personal number in url
                        size="xs"
                      >
                        <IconArrowUpRight size={16} color="blue" />
                      </Anchor>
                    </Table.Td>
                  </Table.Tr>
                ))}
              </Table.Tbody>
            </Table>
          </Table.ScrollContainer>

          <Pagination
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
