
import { useEffect, useState } from "react";
import {  Group, Stack, Button, Title, Alert, Table, Center, Loader, TextInput, Text} from "@mantine/core";
import { IconSearch } from '@tabler/icons-react';
import {IconEdit} from '@tabler/icons-react';
import AddTypeModal from "../../components/AddTypeModal";
import EditTypeModal from "../../components/EditTypeModal";
import { useDisclosure } from '@mantine/hooks'
import api from "../../configs/api";

export default function PreviewOfTypes() {
  const [types, setTypes] = useState([]);
  const [opened, { open, close }] = useDisclosure(false);

  const [editOpened, { open: openEdit, close: closeEdit }] = useDisclosure(false);
  const [selectedTypeId, setSelectedTypeId] = useState('');

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [search, setSearch] = useState('');
  const filtered = types.filter((item) =>
    [item.typeName, item.units]
      .some((value) => value?.toLowerCase().includes(search.toLowerCase()))
  );
  
  useEffect(() => {
    async function fetchTypes() {
    try {
      const response = await api.get('/measurement-types');
      setTypes(response.data);
      
    } catch (err) {
      if (err.response && err.response.data.message) {
          setError(err.response.data.message);
      } else {
          setError('Nepodarilo sa načítať dáta');
  }
    } finally {
      setLoading(false);
    }
  }
  fetchTypes();
  }, []);

  if (loading) return (
        <Center h="100vh">
                <Loader color="#0b5942" />
        </Center>
  );

  if (types.length === 0) {
    return (
      <Stack p="md">
       <AddTypeModal opened={opened} onClose={close}/> 
        <Group justify="space-between">
          <Title order={2}>Prehľad typov meraní</Title>
          <Button
            color='#0b5942'
            p="xs"
            onClick={open}
          >
            Pridať typ merania
          </Button>
        </Group>
        <Alert bg="#e5646f61" >
          Žiadne typy meraní nenájdené
        </Alert>
      </Stack>
    );
  }

  return (
    <Stack p="md">
      <AddTypeModal opened={opened} onClose={close}/>
       {selectedTypeId !== '' ? (<EditTypeModal opened={editOpened} onClose={closeEdit} typeId={selectedTypeId} /> ) : null}
      <Group justify="space-between">
        <Title order={2}>Prehľad typov meraní</Title>
        <Button
          bg="#0b5942"
          c="white"
          p="xs"
          onClick={open}
        >
          Pridať typ merania
        </Button>
      </Group>

      <Alert color="red" hidden={!error}>
        {error}
      </Alert>

      <TextInput  
        placeholder="Hľadať..."
        leftSection={<IconSearch size={16} />}
        value={search}
        onChange={(e) => setSearch(e.currentTarget.value)}
      />

     <Table.ScrollContainer minWidth={400} type="native">
      <Table highlightOnHover>
        <Table.Thead bg="#0b5942" c="white">
          <Table.Tr>
            <Table.Th>Názov typu</Table.Th>
            <Table.Th>Jednotky</Table.Th>
            <Table.Th>Minimálna hodnota</Table.Th>
            <Table.Th>Maximálna hodnota</Table.Th>
            <Table.Th>Akcie</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {filtered.map((t) => (
            <Table.Tr key={t.id} >
              <Table.Td>{t.typeName}</Table.Td>
              <Table.Td>{t.units}</Table.Td>
              <Table.Td>{t.minValue}</Table.Td>
              <Table.Td>{t.maxValue}</Table.Td>
              <Table.Td>
                <Button
                  variant="subtle"
                  color="#0b5942"
                  onClick={() => {
                    console.log(t.id);
                    setSelectedTypeId(t.id); openEdit();}}
                >
                  <IconEdit size={16} />
                   <Text size="xs" ml="xs">Upraviť</Text>
                </Button>
              </Table.Td>

            </Table.Tr>
          ))}
        </Table.Tbody>
      </Table>
     </Table.ScrollContainer> 
    </Stack>
  );
}
