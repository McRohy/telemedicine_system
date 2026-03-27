import { Stack, Title, Text, Center, Group, Button } from '@mantine/core';
import { useNavigate } from 'react-router-dom';
import { IconMoodPuzzled } from '@tabler/icons-react';
import { useAuth } from '../../context/AuthContext';

/** 
 * 404 Not Found error page. 
 * Contains a message button to go back to the previous page.
 */
export default function NotFound() {
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
          <IconMoodPuzzled size={48} stroke={1} />
          <Title order={1} c="black">
             404 Not Found
          </Title>
        </Group>
        <Text>Stránka nebola nájdená.</Text>
        <Button bg="white" onClick={handleGoBack}>
          Späť
        </Button>
      </Stack>
    </Center>
  );
}
