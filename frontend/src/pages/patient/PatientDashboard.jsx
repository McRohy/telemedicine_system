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
  Box,
} from '@mantine/core';
import { MonthPickerInput } from '@mantine/dates';
import { useDisclosure } from '@mantine/hooks';
import { IconCalendarEvent } from '@tabler/icons-react';
import { useAuth } from '../../context/AuthContext';
import api from '../../configs/api';
import { LineChart } from '@mantine/charts';
import TrackMeasurementModal from '../../components/TrackMeasurementModal';
import { notifyError } from '../../configs/notificationHelper';
import 'dayjs/locale/sk';

export default function PatientDashboard() {
  const { user } = useAuth();
  const personalNumber = user?.identificationNumber;

  const [createPlan, { open: openCreatePlan, close: closeCreatePlan }] =
    useDisclosure(false);
  const [plan, setPlan] = useState(null);
  const [loading, setLoading] = useState(true);

  const [measurements, setMeasurements] = useState([]);
  const [period, setPeriod] = useState(null);
  const [filterType, setFilterType] = useState('');
  const [loadingChart, setLoadingChart] = useState(false);

  useEffect(() => {
    async function getMeasurementPlan() {
      try {
        const response = await api({
          url: '/measurement-plans',
          method: 'get',
          params: {
            personalNumber: personalNumber,
          },
        });
        setPlan(response.data);
      } catch (error) {
        notifyError(error);
      } finally {
        setLoading(false);
      }
    }
    getMeasurementPlan();
  }, [personalNumber]);

  async function getMeasurements() {
    console.log(period, filterType);
    setLoadingChart(true);
    try {
      const response = await api({
        url: '/measurements',
        method: 'get',
        params: {
          personalNumber: personalNumber,
          typeId: Number(filterType),
          period: period,
        },
      });
      setMeasurements(response.data);
    } catch (error) {
      notifyError(error);
    } finally {
      setLoadingChart(false);
    }
  }

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
        <Button
          bg="#0b5942"
          c="white"
          p="xs"
          disabled={plan === null}
          onClick={openCreatePlan}
        >
          {plan === null ? 'Nie je priradený plán' : 'Zaznamenať meranie'}
        </Button>
      </Group>

      {plan !== null && (
        <TrackMeasurementModal
          opened={createPlan}
          onClose={closeCreatePlan}
          plan={plan}
        />
      )}

      <Card withBorder radius="md" shadow="sm" p="md" mt="md">
        <Group>
          <IconCalendarEvent size={96} />
          <Stack>
            <Title order={4} mt="sm">
              Aktuálny plán meraní
            </Title>
            {plan === null ? (
              <Text>Žiaden priradený plán</Text>
            ) : (
              <Group mb="xs">
                <Group>
                  <Stack>
                    <Text size="sm">Frekvencia:</Text>
                    <Text size="sm">Čas plánu merania:</Text>
                    <Text size="sm">Typy meraní:</Text>
                  </Stack>
                  <Stack>
                    <Text size="sm">{plan?.frequency}</Text>
                    <Text size="sm">
                      {plan?.timesOfPlannedMeasurements.join(', ')}
                    </Text>
                    <Text size="sm">
                      {plan?.typesOfMeasurements
                        .map((t) => t.typeName)
                        .join(', ')}
                    </Text>
                  </Stack>
                </Group>
              </Group>
            )}
          </Stack>
        </Group>
      </Card>

      <Card shadow="sm" p="md" radius="md" withBorder>
        <Stack>
          <Title order={3}>Graf meraní</Title>
          <Stack>
            <Group grow mb="md">
              <MonthPickerInput
                label="Mesiac"
                placeholder="Vyberte mesiac"
                locale="sk"
                clearable
                value={period}
                onChange={setPeriod}
              />
              <Select
                label="Typ merania"
                placeholder="Vyberte typ merania"
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
            </Group>
            <Button
              variant="outline"
              color="#0b5942"
              p="xs"
              size="sm"
              loading={loadingChart}
              disabled={!filterType || !period}
              onClick={() => getMeasurements()}
            >
              Zobraziť
            </Button>
          </Stack>

          <Box h={300} pos="relative">
            {measurements.length > 0 ? (
              <LineChart
                h={300}
                withPointLabels
                data={measurements}
                dataKey="timeOfMeasurement"
                series={[
                  { name: 'value', color: 'orange', label: 'hodnota merania' },
                ]}
                valueFormatter={(value) => `${value} ${measurements[0].units}`}
              />
            ) : (
              <Center h="100%">
                <Text size="sm" c="dimmed">
                  Žiadne merania nenájdené.
                </Text>
              </Center>
            )}
          </Box>
        </Stack>
      </Card>
    </Stack>
  );
}
