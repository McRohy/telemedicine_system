import { useEffect, useState } from 'react';
import { Group, Stack, Button, Title, Card, Text, Loader, Center } from '@mantine/core';
import { IconArrowLeft, IconUserCircle, IconMail, IconNotebook, IconUser } from '@tabler/icons-react';
import { useDisclosure } from '@mantine/hooks';
import { useNavigate, useParams } from 'react-router-dom';
import PlanModal from '../../components/PlanModal';
import { notifyError } from '../../helpers/notificationHelper';
import MeasurementChart from '../../components/MeasurementChart';
import { getMeasurementPlanByPersonalNumber } from '../../api/measurementPlanApi';
import { getPatientByPersonalNumber } from '../../api/patientApi';
import MeasurementTable from '../../components/MeasurementTable';

export default function PatientDetail() {
  const { personalNumber } = useParams();
  const navigate = useNavigate();
  const [opened, { open: openPlanModal, close: closePlanModal }] =
    useDisclosure(false);

  const [patientData, setPatientData] = useState(null);
  const [plan, setPlan] = useState(null);
  const [loading, setLoading] = useState(true);

  async function fetchPlan() {
    try {
      const response = await getMeasurementPlanByPersonalNumber(personalNumber);
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
      const response = await getPatientByPersonalNumber(personalNumber);
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

  if (loading)
    return (
      <Center h="100vh">
        <Loader />
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
            <IconUserCircle size={64} stroke={1} />
            <Stack>
              <Group>
                <Title order={4}>{patientData.personalData.firstName}</Title>
                <Title order={4}>{patientData.personalData.lastName}</Title>
              </Group>

              <Group>
                <Group gap={3}>
                  <IconUser size={16} />
                  <Text size="sm">
                    {patientData.gender === 'MALE' ? 'Muž' : 'Žena'}
                  </Text>
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
                  <Text size="sm">
                    {plan?.frequency === 'ONE_TIME_DAILY' ? '1x denne': '2x denne'}
                  </Text>
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

          <Button variant="light" c="black" size="xs" onClick={openPlanModal}>
            {plan ? 'Upraviť plán' : 'Vytvoriť plán'}
          </Button>
        </Stack>
      </Card>

      <MeasurementChart personalNumber={personalNumber} plan={plan} />

      <MeasurementTable personalNumber={personalNumber} plan={plan}/>
    </Stack>
  );
}
