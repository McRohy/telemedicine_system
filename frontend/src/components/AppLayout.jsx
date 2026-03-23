import Sidebar from './Sidebar';
import { AppShell, Burger, Group } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { Outlet } from 'react-router-dom';
import { IconHeartbeat } from '@tabler/icons-react';
import { Notifications } from '@mantine/notifications';

export default function AppLayout() {
  const [opened, { toggle, close }] = useDisclosure();

  return (
    <>
      <Notifications />
      <AppShell
        header={{ height: 56 }}
        navbar={{
          width: 300,
          breakpoint: 'sm',
          collapsed: { mobile: !opened },
        }}
      >
        <AppShell.Header>
          <Group h="100%" px="md">
            <Burger
              opened={opened}
              onClick={toggle}
              hiddenFrom="sm"
              size="sm"
            />
            <IconHeartbeat size={32} stroke={1.5}/>
          </Group>
        </AppShell.Header>

        <AppShell.Navbar bg="primary" p="md">
          <Sidebar onClose={close} />
        </AppShell.Navbar>

        <AppShell.Main>
          <Outlet />
        </AppShell.Main>
      </AppShell>
    </>
  );
}
