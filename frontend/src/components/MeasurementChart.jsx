import { useState } from 'react';
import { Group, Stack, Button, Title, Card, Select, Center, Text, Box } from '@mantine/core';
import { MonthPickerInput } from '@mantine/dates';
import api from '../configs/api';
import { LineChart } from '@mantine/charts';
import { notifyError } from '../configs/notificationHelper';
import 'dayjs/locale/sk';

export default function Chart({ personalNumber, plan }) {
  const [measurements, setMeasurements] = useState([]);
  const [period, setPeriod] = useState(null);
  const [filterType, setFilterType] = useState('');
  const [loadingChart, setLoadingChart] = useState(false);
  const [pressToShow, setPressToShow] = useState(false);

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

  return (
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
            p="xs"
            size="sm"
            loading={loadingChart}
            disabled={!filterType || !period}
            onClick={() => {
              getMeasurements();
              setPressToShow(true);
            }}
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
                {pressToShow ? 'Žiadne merania pre zobrazenie v grafe' : 'Zvoľte mesiac a typ merania a kliknite na "Zobraziť" pre zobrazenie grafu'}
              </Text>
            </Center>
          )}
        </Box>
      </Stack>
    </Card>
  );
}
