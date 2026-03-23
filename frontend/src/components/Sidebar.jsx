import { Stack, Group, Text } from '@mantine/core';
import { useLocation, useNavigate } from 'react-router-dom';
import { NavLink } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import ConfirmModal from './ConfirmModal';
import { IconArticle, IconActivity, IconUserCircle, IconUsers, IconStethoscope } from '@tabler/icons-react';
import { useAuth } from '../context/AuthContext';
import { ROLES } from '../helpers/constants';

const navLinks = {
  ADMIN: [
    { label: 'Prehľad lekárov', path: '/admin/doctors', icon: IconStethoscope },
    { label: 'Prehľad pacientov', path: '/admin/patients', icon: IconUsers },
    {
      label: 'Prehľad typov meraní',
      path: '/admin/types-of-measurements',
      icon: IconActivity,
    },
  ],
  DOCTOR: [
    { label: 'Pacienti', path: '/doctor/dashboard', icon: IconUsers },
    { label: 'Články', path: '/doctor/articles', icon: IconArticle },
  ],
  PATIENT: [
    { label: 'Prehľad meraní', path: '/patient/dashboard', icon: IconActivity},
    { label: 'Články', path: '/patient/articles', icon: IconArticle },
  ],
};

export default function Sidebar({ onClose }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const links = user ? navLinks[user.role] : [];
  const [isConfirmModalOpen, { open: openModal, close: closeModal }] = useDisclosure(false);

  return (
    <Stack h="100%" justify="space-between">
      <Stack gap="md">
        <Group>
          <IconUserCircle size={64} stroke={1} />
          <Stack gap={0}>
            <Text size="md" fw={600}>
              {user?.firstName} {user?.lastName}
            </Text>
            <Text size="xs">
              {ROLES[user?.role]}
            </Text>
          </Stack>
        </Group>

        {links.map((link) => (
          <NavLink
            key={link.path}
            label={
              <Text size="md" fw={500}>
                {link.label}
              </Text>
            }
            leftSection={<link.icon size={24} />}
            active={location.pathname === link.path}
            onClick={() => { navigate(link.path); onClose(); }}
            p="md"
            color="#486d7c51"
            variant="filled"
          />
        ))}
      </Stack>

      <Text
        fw={500}
        size="md"
        ta="center"
        mb="sm"
        style={{ cursor: 'pointer' }}
        onClick={openModal}
      >
        Odhlásiť sa
      </Text>

      <ConfirmModal
        opened={isConfirmModalOpen}
        onClose={closeModal}
        onConfirm={() => logout()}
        title="Potvrdenie odhlásenia"
        message="Chcete sa odhlásiť?"
        buttonText="Odhlásiť sa"
      />
    </Stack>
  );
}
