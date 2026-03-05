import { Stack, Group, Text } from "@mantine/core";
import { useLocation, useNavigate } from "react-router-dom";
import { NavLink } from "@mantine/core";
import { IconArticle, IconActivity, IconClipboardHeart, IconUserCircle, IconUsers} from "@tabler/icons-react";

const navLinks = {
  ADMIN: [
    { label: "Prehľad lekarov", path: "/admin/doctors", icon: IconUsers},
    { label: "Prehľad pacientov", path: "/admin/patients", icon: IconUsers},

  ],
  DOCTOR: [
    { label: 'Pacienti', path: '/doctor/patients', icon: IconUsers },
    { label: 'Články', path: '/doctor/articles', icon: IconArticle },
  ],
  PATIENT: [
    { label: 'Prehľad meraní', path: '/patient/measurements', icon: IconActivity },
    { label: 'Články', path: '/patient/articles', icon: IconArticle },
    { label: 'Plán monitorovania', path: '/patient/monitoring-plan', icon: IconClipboardHeart },
  ],
};

export default function Sidebar({ role }) {
  const navigate = useNavigate();
  const location = useLocation();
  const links = navLinks[role] || [];

  return (
    <Stack h="100%" justify="space-between">
      <Stack gap="md">
        <Group>
          <IconUserCircle size={56} color="white" stroke={1} />
          <Stack gap={0}>
            <Text c="white" size="md" fw={600}>
              Matej Bohaty 
            </Text>
            <Text c="white" size="xs">
              {role}
            </Text>
          </Stack>
        </Group>

        {links.map((link) => (
          <NavLink
            key={link.path}
            label={<Text c="white" size="md">{link.label}</Text>}
            leftSection={<link.icon size={20} color="white" />}
            active={location.pathname === link.path}
            onClick={() => navigate(link.path)}
            p="md"
            color="#00000051"
            variant="filled"
            
          />
        ))}
      </Stack>

      <Text
        c="white"
        fw={500}
        size="md"
        ta="center"
        mb="sm"
        style={{ cursor: 'pointer' }}
        onClick={() => navigate('/login')}
      >
        Odhlásiť sa
      </Text>
    </Stack>
  );
}
