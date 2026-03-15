import { BsHeartPulse } from 'react-icons/bs';
import { useState } from 'react';
import { Button, Card, Stack, PasswordInput, Title, Text, TextInput, Center, Alert } from '@mantine/core';
import { useAuth } from '../../context/AuthContext';

function LoginPage() {
  const { login, error } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  return (
    <Center w="100vw" h="100vh" bg="primary">
      <Card p="lg" radius="lg" maw={{ base: 300, xs: 450 }}>
        <Stack gap="xl">
          <Stack align="center" gap="xs" m="xs">
            <BsHeartPulse size={96} />
            <Title order={1}>TELEMEDICINE</Title>
            <Text align="center">
              Vitajte, pre používanie telemedicínskeho systému sa prosím
              prihláste pomocou svojho hesla.
            </Text>
          </Stack>

          <Stack gap="md">
            <TextInput
              label="Email"
              placeholder="rohy@gmail.sk"
              type="email"
              ta="left"
              size="md"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />

            <PasswordInput
              label="Heslo"
              placeholder="**********"
              ta="left"
              size="md"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              color="gray"
            />

            <Button
              type="submit"
              size="md"
              disabled={email == '' || password == ''}
              onClick={() => login({ email, password })}
            >
              Prihlásiť sa
            </Button>
            {error && <Alert color="red">{error}</Alert>}
          </Stack>
        </Stack>
      </Card>
    </Center>
  );
}

export default LoginPage;
