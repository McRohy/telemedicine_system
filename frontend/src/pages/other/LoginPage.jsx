import { BsHeartPulse } from 'react-icons/bs';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Button,
  Card,
  Stack,
  PasswordInput,
  Title,
  Text,
  TextInput,
  Center,
  Alert,
} from '@mantine/core';
import { useAuth } from '../../context/AuthContext';

function LoginPage() {
  const { user, login } = useAuth();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);

  const navigate = useNavigate();

  async function handleLogin() {
    try {
      await login({ email, password });
      if (user.role === 'DOCTOR') {
        navigate('/doctor/dashboard');
      } else if (user.role === 'ADMIN') {
        navigate('/admin/doctors');
      } else if (user.role === 'PATIENT') {
        navigate('/patient/dashboard');
      }
    } catch (err) {
      console.log(err.response);
      setError(err.response?.data?.message || 'Nastala chyba pri prihlásovaní');
    }
  }

  return (
    <Center w="100vw" h="100vh" bg="#0b5942">
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
              color="#0b5942"
              disabled={email == '' || password == ''}
              onClick={() => handleLogin()}
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
