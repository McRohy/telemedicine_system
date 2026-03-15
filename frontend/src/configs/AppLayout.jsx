import Sidebar from '../components/Sidebar';
import { AppShell, Burger, Group, Title } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { Outlet } from 'react-router-dom';
import {BsHeartPulse} from "react-icons/bs";
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
          <BsHeartPulse size={24}/>
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