import { Stack, Title, Text, Center } from "@mantine/core";

export default function Forbidden() {
  return (
    <Center w="100vw" h="100vh" bg="primary">
        <Stack gap="xl" align="center">
           <Title order={1} c="black">403 Forbidden</Title>
           <Text c="white">Nemáte povolenie na prístup k tejto stránke.</Text>
        </Stack>
 
    </Center>
  );
}