import { BsHeartPulse } from "react-icons/bs";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  Button,
  Card,
  Stack,
  PasswordInput,
  Title,
  Text,
  Center,
  Alert,
} from "@mantine/core";

function PasswordPage() {
  const { token } = useParams();
  const [password, setPassword] = useState("");
  const navigate = useNavigate();
  const [alert, setAlert] = useState(null);

  async function sendPassword() {
    const res = await fetch("http://localhost:8080/api/set-password", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ token, password }),
    });

    if (res.ok) {
      navigate("/login");
    } else {
      const error = await res.json();
      setAlert(error.message);
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
              disabled={password == ""}
              onClick={() => sendPassword()}
            >
              Prihlásiť sa
            </Button>
          </Stack>
        </Stack>
        <Alert color="red" hidden={!alert} mb="md">
          {alert}
        </Alert>
      </Card>
    </Center>
  );
}

export default PasswordPage;
