import { Stack, Title, Text, Center } from "@mantine/core";

function Unauthorized() {
  return (
    <Center w="100vw" h="100vh" bg="#0b5942">
        <Stack gap="xl" align="center">
           <Title order={1} c="white">403 Unauthorized</Title>
           <Text c="white">Nemáte povolenie na prístup k tejto stránke.</Text>
        </Stack>
 
    </Center>
  );
}

export default Unauthorized;
