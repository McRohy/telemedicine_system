import { useEffect, useState } from 'react';
import { Group, Stack, Button, Title, Card, Loader, Center, Text, Box } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { IconCalendarEvent } from '@tabler/icons-react';
import { useAuth } from '../../context/AuthContext';
import { getMeasurementPlanByPersonalNumber } from '../../api/measurementPlanApi';
import TrackMeasurementModal from '../../components/TrackMeasurementModal';
import { notifyError } from '../../helpers/notificationHelper';
import MeasurementChart from '../../components/MeasurementChart';
import MeasurementTable from '../../components/MeasurementTable';
import PlanInfoCard from '../../components/PlanInfoCard';

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
        setPlan(response.data);
      } catch (error) {
        if (error.response?.status === 404) {
          setPlan(null);
        } else {
          notifyError(error);
        }
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
          <Box hiddenFrom="sm"><IconCalendarEvent size={48} /></Box>
          <Box visibleFrom="sm"><IconCalendarEvent size={96} /></Box>
          <Stack>
            <Title order={4} mt="sm">
              Aktuálny plán meraní
            </Title>
            <PlanInfoCard plan={plan} />
          </Stack>
        </Group>
      </Card>
      <MeasurementChart personalNumber={personalNumber} plan={plan} />
      <MeasurementTable personalNumber={personalNumber} plan={plan} refresh={refresh}/>
    </Stack>
  );
}
