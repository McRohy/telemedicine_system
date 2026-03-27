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
import PlanInfoCard from '../../components/PlanInfoCard';

/**
 * Doctor page for viewing patient details.
 * Contains patient information, measurement plan and measurement data.
 */
export default function PatientDetail() {
  const { id } = useParams();
  const personalNumber = atob(decodeURIComponent(id)); //decode personal number from url
  const navigate = useNavigate();
  const [isModalOpen, { open: openModal, close: closeModal }] = useDisclosure(false);

  const [patientData, setPatientData] = useState(null);
  const [plan, setPlan] = useState(null);
  const [planLoading, setPlanLoading] = useState(true);
  const [dataLoading, setDataLoading] = useState(true);
  const [refresh, setRefresh] = useState(0);

  useEffect(() => {
    async function fetchPlan() {
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
      setPlanLoading(false);
    }
  }
    fetchPlan();
  }, [personalNumber, refresh]);

  useEffect(() => {
    async function fetchPatientData() {
     try {
      const response = await getPatientByPersonalNumber(personalNumber);
      setPatientData(response.data);
      console.log(response.data);
    } catch (error) {
      notifyError(error);
    } finally {
      setDataLoading(false);
    }
  }
    fetchPatientData();
  }, [personalNumber]);

  if (dataLoading && planLoading)
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

     
      {isModalOpen && (
        <PlanModal
          opened={isModalOpen}
          onClose={() => closeModal()}
          onSuccess={() => setRefresh((r) => r + 1)}
          personalNumber={patientData.personalNumber}
          plan={plan}
        />
      )}
 
      <Card shadow="sm" p="md" radius="md" withBorder>
        <Stack>
          <Group>
            <IconUserCircle size={64} stroke={1} />
            <Stack>
              <Group>
                <Title order={4}>{patientData?.personalData.firstName}</Title>
                <Title order={4}>{patientData?.personalData.lastName}</Title>
              </Group>

              <Group>
                <Group gap={3}>
                  <IconUser size={16} />
                  <Text size="sm">
                    {patientData?.gender === 'MALE' ? 'Muž' : 'Žena'}
                  </Text>
                </Group>
                <Group gap={3}>
                  <IconNotebook size={16} />
                  <Text size="sm">{patientData?.personalNumber}</Text>
                </Group>
                <Group gap={3}>
                  <IconMail size={16} />
                  <Text size="sm">{patientData?.personalData.email}</Text>
                </Group>
              </Group>
            </Stack>
          </Group>
        </Stack>
      </Card>

      <Card shadow="sm" p="md" radius="md" withBorder>
        <Stack>
          <Title order={6}>Monitorovací plán</Title>
          <PlanInfoCard plan={plan}/>

          <Button variant="light" c="black" size="xs" onClick={() => openModal()}>
            {plan ? 'Upraviť plán' : 'Vytvoriť plán'}
          </Button>
        </Stack>
      </Card>

      <MeasurementChart personalNumber={personalNumber} plan={plan} />
      <MeasurementTable personalNumber={personalNumber} plan={plan} refresh={refresh}/>
    </Stack>
  );
}
