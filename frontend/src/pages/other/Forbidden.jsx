import { Stack, Title, Text, Center, Group } from '@mantine/core';
import { IconHandStop } from '@tabler/icons-react';

export default function Forbidden() {
  return (
    <Center w="100vw" h="100vh" bg="primary">
      <Stack gap="xl" align="center">
        <Group>
          <IconHandStop size={48} stroke={1} />
          <Title order={1} c="black">
            403 Forbidden
          </Title>
        </Group>
        <Text>Nemáte povolenie na prístup k tejto stránke.</Text>
      </Stack>
    </Center>
  );
}
