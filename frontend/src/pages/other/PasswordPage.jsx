import { IconHeartbeat } from '@tabler/icons-react';
import { useState } from 'react';
import { useForm } from '@mantine/form';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Card, Stack, PasswordInput, Title, Text, Center, Box } from '@mantine/core';
import { Notifications } from '@mantine/notifications';
import { notifySuccess, notifyError } from '../../helpers/notificationHelper';
import { setPassword } from '../../api/authApi';

/** 
 * Password reset page for user authentication.
 * Contains a form with password, confirm password fields and submit button to set new password.
 */
export default function PasswordPage() {
  const { token } = useParams();
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const form = useForm({
    initialValues: {
      password: '',
      confirmPassword: '',
      token: token,
    },
    validate: {
      password: (value) => (value ? null : 'Povinné pole'),
      confirmPassword: (value, values) =>
        value === values.password ?  null : 'Heslá sa nezhodujú',
    },
  });

  async function postPassword() {
    setLoading(true);
    try {
      await setPassword({ password: form.values.password, token: form.values.token });
      notifySuccess('Heslo nastavené', 'Vaše heslo bolo úspešne nastavené.');
      setTimeout(() => navigate('/login'), 2000); //delay because of notification to be shown
    } catch (error) {
      const status = error.response?.status;

      if (status === 400) {
        form.setErrors(error.response.data.fieldErrors);
      } else {
        notifyError(error);
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <>
    <Notifications />
    <Center w="100vw" h="100vh" bg="primary">
      <Card p="lg" radius="lg" maw={{ base: 300, xs: 450 }}>
        <Stack gap="xl">
          <Stack align="center" gap="xs" m="xs">
            <Box hiddenFrom="sm"><IconHeartbeat size={48} stroke={1.5}/></Box>
            <Box visibleFrom="sm"><IconHeartbeat size={96} stroke={1.5}/></Box>
            <Title order={1}>MediRoh</Title>
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

              <PasswordInput
                label="Potvrdenie hesla"
                placeholder="Zopakujte nové heslo"
                ta="left"
                size="md"
                withAsterisk
                {...form.getInputProps('confirmPassword')}
              />

              <Button type="submit" size="md" loading={loading}>
                Nastaviť heslo
              </Button>
            </Stack>
          </form>
        </Stack>
      </Card>
    </Center>
    </>
  );
}
