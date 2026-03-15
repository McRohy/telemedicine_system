import { useEffect, useState } from 'react';
import { Group, Stack, Button, Title, Card, Loader, Center, Text } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { IconCalendarEvent } from '@tabler/icons-react';
import { useAuth } from '../../context/AuthContext';
import api from '../../configs/api';
import TrackMeasurementModal from '../../components/TrackMeasurementModal';
import { notifyError } from '../../configs/notificationHelper';
import MeasurementChart from '../../components/MeasurementChart';

export default function PatientDashboard() {
  const { user } = useAuth();
  const personalNumber = user?.identificationNumber;

  const [trackMeasurement, { open, close }] = useDisclosure(false);
  const [plan, setPlan] = useState(null);
  const [loading, setLoading] = useState(true);

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
    getMeasurementPlan();
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
        <Button p="xs" disabled={plan === null} onClick={open}>
          {plan === null ? 'Nie je priradený plán' : 'Zaznamenať meranie'}
        </Button>
      </Group>

      <TrackMeasurementModal
        opened={trackMeasurement}
        onClose={close}
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
    </Stack>
  );
}
