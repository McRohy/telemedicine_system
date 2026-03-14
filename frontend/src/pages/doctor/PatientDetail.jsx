import { useEffect, useState } from 'react';
import { Group, Stack, Button, Title, Card, Text, Loader, Center, Box, Select } from '@mantine/core';
import { IconArrowLeft, IconUserCircle, IconMail, IconNotebook, IconUser } from '@tabler/icons-react';
import { useDisclosure } from '@mantine/hooks';
import { useNavigate, useParams } from 'react-router-dom';
import { LineChart } from '@mantine/charts';
import PlanModal from '../../components/PlanModal';
import { MonthPickerInput } from '@mantine/dates';
import { notifyError } from '../../configs/notificationHelper';
import api from '../../configs/api';

export default function PatientDetail() {
  const { personalNumber } = useParams();
  const navigate = useNavigate();
  const [opened, { open: openPlanModal, close: closePlanModal }] =
    useDisclosure(false);

  const [patientData, setPatientData] = useState(null);
  const [plan, setPlan] = useState(null);
  const [loading, setLoading] = useState(true);

  const [measurements, setMeasurements] = useState([]);
  const [period, setPeriod] = useState(null);
  const [filterType, setFilterType] = useState('');
  const [loadingChart, setLoadingChart] = useState(false);

  async function fetchPlan() {
    try {
      const response = await api({
        url: '/measurement-plans',
        method: 'get',
        params: { personalNumber },
      });

      if (response.status === 204) {
        setPlan(null);
      } else {
        setPlan(response.data);
      }
    } catch (error) {
      notifyError(error);
    }
  }

  async function fetchData() {
    try {
      const response = await api({
        url: `/patients/${personalNumber}`,
        method: 'get',
      });
      setPatientData(response.data);
    } catch (error) {
      notifyError(error);
    }
  }

  useEffect(() => {
    async function init() {
      await Promise.all([fetchData(), fetchPlan()]);
      setLoading(false);
    }
    init();
  }, [personalNumber]);

  async function getMeasurementPlan() {
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
      <Group>
        <IconArrowLeft onClick={() => navigate(-1)}></IconArrowLeft>
        <Title order={4}>Prehľad pacienta</Title>
      </Group>

      <PlanModal
        opened={opened}
        onClose={() => {
          closePlanModal();
          fetchPlan(); // refresh the plan after closing the modal to show updated data
        }}
        panNumber={patientData.doctorPanNumber}
        personalNumber={personalNumber}
        plan={plan}
      />

      <Card shadow="sm" p="md" radius="md" withBorder>
        <Stack>
          <Group>
            <IconUserCircle size={64} />
            <Stack>
              <Group>
                <Title order={4}>{patientData.personalData.firstName}</Title>
                <Title order={4}>{patientData.personalData.lastName}</Title>
              </Group>

              <Group>
                <Group gap={3}>
                  <IconUser size={16} />
                  <Text size="sm">{patientData.gender === 'MALE' ? 'Muž' : 'Žena'}</Text>
                </Group>
                <Group gap={3}>
                  <IconNotebook size={16} />
                  <Text size="sm">{patientData.personalNumber}</Text>
                </Group>
                <Group gap={3}>
                  <IconMail size={16} />
                  <Text size="sm">{patientData.personalData.email}</Text>
                </Group>
              </Group>
            </Stack>
          </Group>
        </Stack>
      </Card>

      <Card shadow="sm" p="md" radius="md" withBorder>
        <Stack>
          <Title order={6}>Monitorovací plán</Title>

          {plan === null ? (
            <Text size="sm" c="dimmed">
              Pacient nemá monitorovací plán.
            </Text>
          ) : (
            <Card withBorder>
              <Group mb="xs">
                <Stack>
                  <Text size="sm">Frekvencia:</Text>
                  <Text size="sm">Čas plánu merania:</Text>
                  <Text size="sm">Typy meraní:</Text>
                  <Text size="sm">Vytvoreny: </Text>
                  <Text size="sm">Posledna úprava:</Text>
                </Stack>
                <Stack>
                  <Text size="sm">{plan?.frequency === 'ONE_TIME_DAILY' ? '1x denne' : '2x denne'}</Text>
                  <Text size="sm">
                    {plan?.timesOfPlannedMeasurements?.join(', ')}
                  </Text>
                  <Text size="sm">
                    {plan?.typesOfMeasurements
                      ?.map((t) => t.typeName)
                      .join(', ')}
                  </Text>
                  <Text size="sm">{plan?.createdAt}</Text>
                  <Text size="sm">{plan?.lastUpdateAt}</Text>
                </Stack>
              </Group>
            </Card>
          )}

          <Button
            variant="light"
            size="xs"
            color="#0b5942"
            onClick={openPlanModal}
          >
            {plan ? 'Upraviť plán' : 'Vytvoriť plán'}
          </Button>
        </Stack>
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
              onClick={() => getMeasurementPlan()}
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
