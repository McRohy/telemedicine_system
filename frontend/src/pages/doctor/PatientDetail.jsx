import { useEffect, useState } from "react";
import { Group, Stack, Button, Title, Card, Text, Loader, Center, Alert, Select, TextInput} from "@mantine/core";
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
import api from "../../configs/api";
import EditPlanModal from "../../components/EditPlanModal";


export default function PatientDetail() {
  const { personalNumber } = useParams();
  const navigate = useNavigate();
  const [createPlan, { open: openCreatePlan, close: closeCreatePlan }] = useDisclosure(false);
  const [editOpened, { open: openEdit, close: closeEdit }] = useDisclosure(false);

  const [patientData, setPatientData] = useState(null);
  const [plan, setPlan] = useState(null);
  //const [planError, setPlanError] = useState(null);
  const [loading, setLoading] = useState(true);

  //const [measurements, setMeasurements] = useState([]);
  const [dateRange, setDateRange] = useState([null, null]);
  const [filterType, setFilterType] = useState(null);
  //const [from, to] = dateRange;
  

  useEffect(() => {
    async function fetchData() {
    try {
      const [patientData, plan] = await Promise.all([
        api.get(`/patients/${personalNumber}`),
        api.get(`/measurement-plans?personalNumber=${personalNumber}`),
       
        
      ]);
      setPatientData(patientData.data);
      setPlan(plan.data);
      //setMeasurements(measurements.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };
  fetchData();
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
    
    <PlanModal opened={createPlan} onClose={closeCreatePlan} panNumber={patientData.doctorPanNumber} personalNumber={personalNumber} />
    <EditPlanModal  opened={editOpened}  onClose={closeEdit}  plan={plan}  />
    
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

           
              <Card withBorder
                 display={plan ? "block" : "none"}
              >
                <Group mb="xs">
                <Stack>
                  <Text size="sm">Frekvencia:</Text>
                  <Text size="sm">Čas plánu merania:</Text>
                  <Text size="sm">Typy meraní:</Text>
                  <Text size="sm">Vytvoreny: </Text>
                  <Text size="sm">Posledna úprava:</Text>
                </Stack>
                <Stack>
                  <Text size="sm">{plan.frequencyDescription}</Text>
                  <Text size="sm">{plan.timesOfPlannedMeasurements?.join(", ")}</Text>
                  <Text size="sm">{plan.typesOfMeasurements?.map(t => t.typeName).join(", ")}</Text>
                  <Text size="sm">{plan.createdAt}</Text>
                  <Text size="sm">{plan.lastUpdateAt}</Text>
                </Stack>
                </Group>
              </Card>
           
              <Text size="sm" c="dimmed"
                display={plan ? "none" : "block"}
              >Pacient nemá monitorovací plán.
              </Text>
           
          
            
          <Button
           variant="light" size="xs" color="#0b5942"
           onClick={plan ? openEdit : openCreatePlan}>
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
                    data={plan?.typesOfMeasurements?.map((t) => ({ value: (t.typeOfMeasurementId ?? t.id)?.toString() ?? '', label: t.typeName ?? '' })) ?? []}
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
