import { useEffect, useState } from 'react';
import { Group, Stack, Button, Title, Card, Loader, Center, Text } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { IconCalendarEvent } from '@tabler/icons-react';
import { useAuth } from '../../context/AuthContext';
import { getMeasurementPlanByPersonalNumber } from '../../api/measurementPlanApi';
import TrackMeasurementModal from '../../components/TrackMeasurementModal';
import { notifyError } from '../../helpers/notificationHelper';
import MeasurementChart from '../../components/MeasurementChart';
import MeasurementTable from '../../components/MeasurementTable';

export default function PatientDashboard() {
  const { user } = useAuth();
  const personalNumber = user?.identificationNumber;

  const [isModalOpen, { open: openModal, close: closeModal }] = useDisclosure(false);
  const [plan, setPlan] = useState(null);
  const [loading, setLoading] = useState(true);
  const [refresh, setRefresh] = useState(0);

  useEffect(() => {
    async function fetchMeasurementPlan() {
      try {
        const response = await getMeasurementPlanByPersonalNumber(personalNumber);
        if (response.status === 204) {
          setPlan(null);
        } else {
          setPlan(response.data);
        }
      } catch (error) {
        notifyError(error);
      } finally {
        setLoading(false);
      }
    }
    fetchMeasurementPlan();
  }, [personalNumber]);

  if (loading)
    return (
      <Center h="100vh">
        <Loader />
      </Center>
    );

  return (
    <Stack p="md">
      <Group justify="space-between">
        <Title order={2}>Prehľad meraní</Title>
        <Button p="xs" disabled={plan === null} onClick={openModal}>
          {plan === null ? 'Nie je priradený plán' : 'Zaznamenať meranie'}
        </Button>
      </Group>

      <TrackMeasurementModal
        opened={isModalOpen}
        onClose={() => closeModal()}
        onSuccess={() => setRefresh((r) => r + 1)}
        plan={plan}
      />

      <Card withBorder radius="md" shadow="sm" p="md" mt="md">
        <Group>
          <IconCalendarEvent size={96} />
          <Stack>
            <Title order={4} mt="sm">
              Aktuálny plán meraní
            </Title>
            {plan === null ? (
              <Text size="sm" c="dimmed">
                Aktuálne žiaden monitorovací plán.
              </Text>
            ) : (
              <Group mb="xs">
                <Group>
                  <Stack>
                    <Text size="sm">Frekvencia:</Text>
                    <Text size="sm">Čas plánu merania:</Text>
                    <Text size="sm">Typy meraní:</Text>
                  </Stack>
                  <Stack>
                    <Text size="sm">
                      {plan?.frequency === 'ONE_TIME_DAILY'? '1x denne' : '2x denne'}
                    </Text>
                    <Text size="sm">
                      {plan?.timesOfPlannedMeasurements.join(', ')}
                    </Text>
                    <Text size="sm">
                      {plan?.typesOfMeasurements .map((t) => t.typeName).join(', ')}
                    </Text>
                  </Stack>
                </Group>
              </Group>
            )}
          </Stack>
        </Group>
      </Card>
      <MeasurementChart personalNumber={personalNumber} plan={plan} />
      <MeasurementTable personalNumber={personalNumber} plan={plan} refresh={refresh}/>
    </Stack>
  );
}
