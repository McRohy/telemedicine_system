import { BsHeartPulse } from "react-icons/bs";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Button, Card, Stack, PasswordInput, Title, Text, Center,} from "@mantine/core";
import { Notifications } from "@mantine/notifications";
import { notifySuccess, notifyError } from "../../configs/notificationHelper";
import api from "../../configs/api";

function PasswordPage() {
  const { token } = useParams();
  const [password, setPassword] = useState("");
  const [errorInput, setErrorInput] = useState(null);
  const navigate = useNavigate();

  async function sendPassword() {
    try {
      await api.post("/auth/set-password", { token, password });
      notifySuccess("Heslo nastavené", "Vaše heslo bolo úspešne nastavené.");
      navigate("/login");
    } catch (err) {
      console.log(err.response);
      const status = err.response?.status;

      if (status === 400) {
        setErrorInput(err.response.data.fieldErrors);
      } else {
        notifyError(err);
      }
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

          <Stack gap="md">
            <PasswordInput
              label="Heslo"
              placeholder="Zadajte nové heslo"
              ta="left"
              size="md"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              withAsterisk
              error={errorInput?.["password"]}
              onClick={() => setErrorInput(null)}
            />

            <Button
              type="submit"
              size="md"
              onClick={() => sendPassword()}
            >
              Nastaviť heslo
            </Button>
          </Stack>
        </Stack>
      </Card>
    </Center>
  );
}

export default PasswordPage;
