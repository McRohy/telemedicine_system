import { useEffect, useState } from 'react';
import { Stack, Card, Text, Loader, Center, Select, Table, Pagination,} from '@mantine/core';
import { notifyError } from '../helpers/notificationHelper';
import { getAllMeasurementsForTable } from '../api/measurementsApi';

/** 
 * Component for displaying measurements in a table. 
 * Contains filter for measurement type.
 */
export default function MeasurementTable({ personalNumber, plan, refresh }) {
  const [page, setPage] = useState(1);
  const [data, setData] = useState({ content: [], totalPages: 0 });
  const [filterType, setFilterType] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchMeasurementsForTable() {
      try {
        const response = await getAllMeasurementsForTable(personalNumber, Number(filterType), page);
        setData(response.data);
      } catch (error) {
        notifyError(error);
      } finally {
        setLoading(false);
      }
    };

    fetchMeasurementsForTable();
  }, [personalNumber, page, filterType, refresh]);

  if (loading)
    return (
      <Center h="100%">
        <Loader />
      </Center>
    );

  return (
    <Card shadow="sm" p="md" radius="md" withBorder>
      <Stack>
        <Select
          placeholder="Vyberte typ merania pre filtrovanie tabuľky"
          data={
            plan ? plan.typesOfMeasurements
            .map((t) => ({ value: String(t.id), label: t.typeName, })) : []
          }
          searchable
          clearable
          value={filterType}
          onChange={setFilterType}
        />

        <Stack mih={450} justify="space-between">
          {data.content.length === 0 ? (
            <Text size="sm" ta="center" c="dimmed">
              Žiadne merania k dispozícii pre zobrazenie v tabuľke
            </Text>
          ) : (
            <>
              <Table.ScrollContainer minWidth={400} type="native">
                <Table highlightOnHover>
                  <Table.Thead bg="primary" c="black">
                    <Table.Tr>
                      <Table.Th>Typ merania</Table.Th>
                      <Table.Th>Hodnota merania</Table.Th>
                      <Table.Th>Čas merania</Table.Th>
                      <Table.Th>Status merania</Table.Th>
                      <Table.Th>Poznámka</Table.Th>
                    </Table.Tr>
                  </Table.Thead>
                  <Table.Tbody>
                    {data.content.map((m) => (
                      <Table.Tr key={m.id}>
                        <Table.Td>{m.typeOfMeasurement.typeName}</Table.Td>
                        <Table.Td>
                          {m.value} {m.typeOfMeasurement.units}
                        </Table.Td>
                        <Table.Td>{m.timeOfMeasurement}</Table.Td>
                        <Table.Td>
                          <Text
                            size="xs"
                            c={m.status === 'ABNORMAL' ? 'red' : 'green'}
                          >
                            {m.status}
                          </Text>
                        </Table.Td>
                        <Table.Td>{m.note}</Table.Td>
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
            </>
          )}
        </Stack>
      </Stack>
    </Card>
  );
}
