import { useEffect, useState } from 'react';
import { Stack, Card, Text, Loader, Center, Select, Table, Pagination,} from '@mantine/core';
import { notifyError } from '../configs/notificationHelper';
import api from '../configs/api';

export default function MeasurementTable({ personalNumber, plan }) {
  const [page, setPage] = useState(1);
  const [data, setData] = useState({ content: [], totalPages: 0 });
  const [filterType, setFilterType] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const getMeasurementsTable = async () => {
      try {
        const response = await api({
          url: '/measurements/table',
          method: 'get',
          params: {
            personalNumber: personalNumber,
            page: page - 1, //React 1, Spring 0
            size: 10,
            typeId: Number(filterType) || undefined,
          },
        });
        setData(response.data);
      } catch (error) {
        notifyError(error);
      } finally {
        setLoading(false);
      }
    };

    getMeasurementsTable();
  }, [personalNumber, page, filterType]);

  if (loading)
    return (
      <Center h="100%">
        <Loader />
      </Center>
    );

  return (
    <Card shadow="sm" p="md" radius="md" withBorder justify="space-between">
      <Stack>
        <Select
          placeholder="Vyberte typ merania pre filtrovanie tabuľky"
          data={
            plan
              ? plan.typesOfMeasurements.map((t) => ({
                  value: String(t.id),
                  label: t.typeName,
                }))
              : []
          }
          searchable
          clearable
          value={filterType}
          onChange={setFilterType}
        />

        <Stack mih={450} justify="space-between">
          {data.content.length === 0 ? (
            <Text size="sm" align="center" c="dimmed">
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
                    {console.log(data.content)}

                    {data.content.map((m) => (
                      <Table.Tr key={m.id}>
                        <Table.Td>{m.typeName}</Table.Td>
                        <Table.Td>
                          {m.value} {m.units}
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
