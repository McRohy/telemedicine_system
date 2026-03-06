import { useEffect, useState } from "react";
import { Group, Stack, Button, Title, Card, Text, Loader, Center, Alert, Select} from "@mantine/core";
import {
  IconArrowLeft,
  IconUserCircle,
  IconMail,
  IconNotebook,
  IconUser,
} from "@tabler/icons-react";
import { useDisclosure } from "@mantine/hooks";
import { useNavigate, useParams } from "react-router-dom";

import { LineChart } from '@mantine/charts';
import PlanModal from "../../components/PlanModal";
import { DatePickerInput } from "@mantine/dates";


export default function PatientDetail() {
  const { personalNumber } = useParams();
  const navigate = useNavigate();
  const [opened, { open, close }] = useDisclosure(false);

  const [patientData, setPatientData] = useState(null);
  const [plan, setPlan] = useState(null);
  const [planError, setPlanError] = useState(null);
  const [loading, setLoading] = useState(true);

  const [measurements, setMeasurements] = useState([]);
  const [dateRange, setDateRange] = useState([null, null]);
  const [filterType, setFilterType] = useState(null);
  const [from, to] = dateRange;
  

  useEffect(() => {
      if (!from || !to || !filterType) return;
      async function loadMeasurements() {
        const res = await fetch(`http://localhost:8080/api/measurements?personalNumber=${personalNumber}&from=${from}&to=${to}&typeId=${filterType}`);
        if (res.ok) {
          const data = await res.json();
          setMeasurements(data);
        }
      }
      loadMeasurements();
    }, [personalNumber, from, to, filterType]);

  useEffect(() => {
      async function loadPatient() {
        const res = await fetch(`http://localhost:8080/api/patients/${personalNumber}`);
        if (res.ok) {
          setPatientData(await res.json());
        }
        setLoading(false);
      }
      loadPatient();
    }, [personalNumber]);

  useEffect(() => {
      async function loadPlan() {
        const res = await fetch(`http://localhost:8080/api/plans/${personalNumber}`);
        if (res.ok) {
          setPlan(await res.json());
        } else if (res.status !== 404) {
          setPlanError("Nepodarilo sa načítať plán.");
        }
      }
      loadPlan();
    }, [personalNumber]);


  if (loading) return (
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
    
    <PlanModal opened={opened} onClose={close} panNumber={patientData.doctorPanNumber} personalNumber={personalNumber} />

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
                  <Text size="sm">{patientData.gender}</Text>
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

        {planError ? (
            <Alert color="red">{planError}</Alert>
          ) : plan === null ? (
            <Text size="sm" c="dimmed">Pacient zatiaľ nemá monitorovací plán.</Text>
          ) : (
            <Card withBorder>
                <Group grow>
                <Text size="sm">Frekvencia: {plan.frequency}</Text>
                <Text size="sm">Čas plánu merania: {plan.timeOfPlannedMeasurements}</Text>
                <Text size="sm">Typy meraní: {plan.typesOfMeasurements?.map((t) => t.typeName).join(", ")}</Text>
              </Group>
            </Card>
          )}
            
          <Button
           variant="light" size="xs" color="#0b5942"
           onClick={open}>
            {plan ? "Upraviť plán" : "Vytvoriť plán"}
          </Button>
        </Stack>
      </Card>

      <Card shadow="sm" p="md" radius="md" withBorder >
        <Stack >
          <Title order={3}>Graf meraní pacienta</Title>
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
                    data={plan ? plan.typesOfMeasurements.map((t) => ({ value: t.typeOfMeasurementId + '', label: t.typeName })) : []}
                    searchable
                    clearable
                    value={filterType}
                    onChange={setFilterType}
            
                 />
            </Group>
        
            <LineChart
                h={300}
                withPointLabels
                data={measurements}
                dataKey="timeOfMeasurement"
                series={[{ name: 'value', color: 'orange' }]}
            />
        </Stack>
      </Card>
    </Stack>
  );
}
