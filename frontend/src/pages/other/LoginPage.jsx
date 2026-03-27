import { IconHeartbeat } from '@tabler/icons-react';
import { useForm } from '@mantine/form';
import { Button, Card, Stack, PasswordInput, Title, Text, TextInput, Center, Alert, Box } from '@mantine/core';
import { useAuth } from '../../context/AuthContext';

/** 
 * Login page for user authentication.
 * Contains a form with email and password fields and a submit button to log in.
 */
export default function LoginPage() {
  const { login, loading, error } = useAuth();

  const formRequest = useForm({
    initialValues: {
      email: '',
      password: '',
    },
    validate: {
      email: (value) => (value ? null : 'Povinné pole'),
      password: (value) => (value ? null : 'Povinné pole'),
    },
  });

  return (
    <Center w="100vw" h="100vh" bg="primary">
      <Card p="lg" radius="lg" maw={{ base: 300, xs: 450 }}>
        <Stack gap="xl">
          <Stack align="center" gap="xs" m="xs">
            <Box hiddenFrom="sm"><IconHeartbeat size={48} stroke={1.5}/></Box>
            <Box visibleFrom="sm"><IconHeartbeat size={96} stroke={1.5}/></Box>
            <Title order={1}>MediRoh</Title>
            <Text align="center">
              Vitajte, pre používanie telemedicínskeho systému sa prosím
              prihláste pomocou svojho hesla.
            </Text>
          </Stack>

          <form onSubmit={formRequest.onSubmit((values) => login(values))}>
            <Stack gap="md">
              <TextInput
                label="Email"
                placeholder="rohy@gmail.sk"
                type="email"
                ta="left"
                size="md"
                {...formRequest.getInputProps('email')}
              />

              <PasswordInput
                label="Heslo"
                placeholder="**********"
                ta="left"
                size="md"
                {...formRequest.getInputProps('password')}
                color="gray"
              />

              <Button type="submit" size="md" loading={loading}>
                Prihlásiť sa
              </Button>
              {error && <Alert color="red">{error}</Alert>}
            </Stack>
          </form>
        </Stack>
      </Card>
    </Center>
  );
}
