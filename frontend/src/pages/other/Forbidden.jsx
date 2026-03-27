import { Stack, Title, Text, Center, Group, Button } from '@mantine/core';
import { useNavigate } from 'react-router-dom';
import { IconHandStop } from '@tabler/icons-react';
import { useAuth } from '../../context/AuthContext';

export default function Forbidden() {
  const navigate = useNavigate();
  const { user } = useAuth();

  function handleGoBack() {
    if (!user) {
      navigate('/login');
    } else if (user.role === 'ADMIN') {
      navigate('/admin/doctors');
    } else {
      navigate(`/${user.role.toLowerCase()}/dashboard`);
    }
  }

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
        <Button bg="white" onClick={handleGoBack}>
          Späť
        </Button>
      </Stack>
    </Center>
  );
}
