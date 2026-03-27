import Sidebar from './Sidebar';
import { AppShell, Burger, Group } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { Outlet } from 'react-router-dom';
import { IconHeartbeat } from '@tabler/icons-react';
import { Notifications } from '@mantine/notifications';

/**
 * AppLayout is layout component that defines structure for pages in the application.
 * Contains a header with burger menu, a collapsible sidebar and a main content area.
 */
export default function AppLayout() {
  const [isSidebarOpened, { toggle, close }] = useDisclosure();

  return (
    <>
      <Notifications />
      <AppShell
        header={{ height: 56 }}
        navbar={{
          width: 300,
          breakpoint: 'sm',
          collapsed: { mobile: !isSidebarOpened },
        }}
      >
        <AppShell.Header>
          <Group h="100%" px="md">
            <Burger
              opened={isSidebarOpened}
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
          <Outlet /> {/*placeholder for content of child routes */}
        </AppShell.Main>
      </AppShell>
    </>
  );
}
