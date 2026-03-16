import { BsHeartPulse } from 'react-icons/bs';
import { useState } from 'react';
import { useForm } from '@mantine/form';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Card, Stack, PasswordInput, Title, Text, Center } from '@mantine/core';
import { Notifications } from '@mantine/notifications';
import { notifySuccess, notifyError } from '../../helpers/notificationHelper';
import { setPassword } from '../../api/authApi';

export default function PasswordPage() {
  const { token } = useParams();
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const form = useForm({
    initialValues: {
      password: '',
      token: token,
    },
    validate: {
      password: (value) => (value ? null : 'Povinné pole'),
    },
  });

  async function postPassword() {
    setLoading(true);
    try {
      await setPassword(form.values);
      notifySuccess('Heslo nastavené', 'Vaše heslo bolo úspešne nastavené.');
      setTimeout(() => navigate('/login'), 2000); //dealay because of notification to be shown
    } catch (error) {
      notifyError(error);
    } finally {
      setLoading(false);
    }
  }

  return (
    <Center w="100vw" h="100vh" bg="primary">
      <Notifications />
      <Card p="lg" radius="lg" maw={{ base: 300, xs: 450 }}>
        <Stack gap="xl">
          <Stack align="center" gap="xs" m="xs">
            <BsHeartPulse size={96} />
            <Title order={1}>TELEMEDICINE</Title>
            <Text align="center">
              Vitajte, pre používanie telemedicínskeho systému si prosím
              nastavte svoje nové heslo.
            </Text>
          </Stack>

          <form onSubmit={form.onSubmit(postPassword)}>
            <Stack gap="md">
              <PasswordInput
                label="Heslo"
                placeholder="Zadajte nové heslo"
                ta="left"
                size="md"
                withAsterisk
                {...form.getInputProps('password')}
              />

              <Button type="submit" size="md" loading={loading}>
                Nastaviť heslo
              </Button>
            </Stack>
          </form>
        </Stack>
      </Card>
    </Center>
  );
}
