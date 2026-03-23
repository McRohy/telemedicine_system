import { Group, Stack, Text } from '@mantine/core';

export default function PlanInfoCard({ plan }) {
  if (plan === null) {
    return (
      <Text size="sm" c="dimmed">
        Aktuálne žiadny monitorovací plán.
      </Text>
    );
  }

  return (
    <Group mb="xs">
      <Stack>
        <Text size="sm">Frekvencia:</Text>
        <Text size="sm">Čas plánu merania:</Text>
        <Text size="sm">Typy meraní:</Text>
        <Text size="sm">Vytvorený:</Text>
      </Stack>
      <Stack>
        <Text size="sm">
          {plan?.frequency === 'ONE_TIME_DAILY' ? '1x denne' : '2x denne'}
        </Text>
        <Text size="sm">{plan?.timesOfPlannedMeasurements.join(', ')}</Text>
        <Text size="sm">
          {plan?.typesOfMeasurements.map((t) => t.typeName).join(', ')}
        </Text>
        <Text size="sm">{plan?.validFrom}</Text>
      </Stack>
    </Group>
  );
}
