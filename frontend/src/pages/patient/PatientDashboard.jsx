import { useEffect, useState } from 'react';
import {
  Group,
  Stack,
  Button,
  Title,
  Card,
  Select,
  Loader,
  Center,
  Text,
  TextInput,
} from '@mantine/core';
import { DatePickerInput } from '@mantine/dates';
import { useDisclosure } from '@mantine/hooks';
import { IconCalendarEvent } from '@tabler/icons-react';
import { useAuth } from '../../context/AuthContext';
import api from '../../configs/api';
import { LineChart } from '@mantine/charts';
import TrackMeasurementModal from '../../components/TrackMeasurementModal';

export default function PatientDashboard() {
  const { user } = useAuth();
  const personalNumber = user?.identificationNumber;

  const [createPlan, { open: openCreatePlan, close: closeCreatePlan }] =
    useDisclosure(false);

  const [plan, setPlan] = useState(null);
  const [loading, setLoading] = useState(true);
  //const [error, setError] = useState(null);

  const [dateRange, setDateRange] = useState([null, null]);
  const [filterType, setFilterType] = useState(null);

  useEffect(() => {
    async function fetchData() {
      try {
        const [plan] = await Promise.all([
          api.get(`/measurement-plans?personalNumber=${personalNumber}`),
        ]);
        setPlan(plan.data);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    }
    fetchData();
  }, [personalNumber]);

  if (loading)
    return (
      <Center h="100vh">
        <Loader color="#0b5942" />
      </Center>
    );

  return (
    <Stack p="md">
      <Group justify="space-between">
        <Title order={2}>Prehľad meraní</Title>
        <Button bg="#0b5942" c="white" p="xs" onClick={openCreatePlan}>
          Zaznamenať meranie
        </Button>
      </Group>

      <TrackMeasurementModal
        opened={createPlan}
        onClose={closeCreatePlan}
        plan={plan}
      />

      <Card withBorder radius="md" shadow="sm" p="md" mt="md">
        <Group>
          <IconCalendarEvent size={96} />
          <Stack>
            <Title order={4} mt="sm">
              Aktuálny plán meraní{' '}
            </Title>
            <Group mb="xs">
              <Group>
                <Stack>
                  <Text size="sm">Frekvencia:</Text>
                  <Text size="sm">Čas plánu merania:</Text>
                  <Text size="sm">Typy meraní:</Text>
                </Stack>
                <Stack>
                  <Text size="sm">{plan.frequency}</Text>
                  <Text size="sm">
                    {plan.timesOfPlannedMeasurements?.join(', ')}
                  </Text>
                  <Text size="sm">
                    {plan.typesOfMeasurements?.map((t) => t.typeName).join(', ')}
                  </Text>
                </Stack>
              </Group>
            </Group>
          </Stack>
        </Group>
      </Card>
      <Card shadow="sm" p="md" radius="md" withBorder>
        <Stack>
          <Title order={3}>Graf meraní </Title>
          <Group grow mb="md">
            <DatePickerInput
              type="range"
              label="Obdobie merania"
              placeholder="Vyber rozsah"
              value={dateRange}
              onChange={setDateRange}
              clearable
            />

            <Select
              label="Typ merania"
              placeholder="Vyberte typ merania"
              data={plan.typesOfMeasurements.map((t) => ({
                value: String(t.id),
                label: t.typeName,
              }))}
              searchable
              clearable
              value={filterType}
              onChange={setFilterType}
            />
          </Group>

          <LineChart
            h={300}
            withPointLabels
            data={[]}
            dataKey="timeOfMeasurement"
            series={[{ name: 'value', color: 'orange' }]}
          />
        </Stack>
      </Card>
    </Stack>
  );
}
