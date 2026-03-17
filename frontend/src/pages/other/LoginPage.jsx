import { BsHeartPulse } from 'react-icons/bs';
import { useForm } from '@mantine/form';
import { Button, Card, Stack, PasswordInput, Title, Text, TextInput, Center, Alert } from '@mantine/core';
import { useAuth } from '../../context/AuthContext';

function LoginPage() {
  const { login, loading, error } = useAuth();

  const form = useForm({
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
            <BsHeartPulse size={96} />
            <Title order={1}>MediRoh</Title>
            <Text align="center">
              Vitajte, pre používanie telemedicínskeho systému sa prosím
              prihláste pomocou svojho hesla.
            </Text>
          </Stack>

          <form onSubmit={form.onSubmit((values) => login(values))}>
            <Stack gap="md">
              <TextInput
                label="Email"
                placeholder="rohy@gmail.sk"
                type="email"
                ta="left"
                size="md"
                {...form.getInputProps('email')}
              />

              <PasswordInput
                label="Heslo"
                placeholder="**********"
                ta="left"
                size="md"
                {...form.getInputProps('password')}
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

export default LoginPage;
