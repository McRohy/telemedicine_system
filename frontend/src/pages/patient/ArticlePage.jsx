import { useEffect, useState } from 'react';
import { Group, Stack, Title, Text, Loader, Center, Alert, Card, Pagination } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { IconFirstAidKit } from '@tabler/icons-react';
import { notifyError } from '../../helpers/notificationHelper';
import { getArticles } from '../../api/articleApi';
import DetailArticleModal from '../../components/DetailArticleModal';

export default function ArticlePage() {
  const [ isModalOpen, { open: openModal, close: closeModal }] = useDisclosure(false);
  const [selectedArticleId, setSelectedArticleId] = useState(null);

  const [loading, setLoading] = useState(true);
  const [refresh, setRefresh] = useState(0);

  const [page, setPage] = useState(1);
  const [data, setData] = useState({ content: [], totalPages: 0 });

  useEffect(() => {
    async function fetchArticles() {
      try {
        const response = await getArticles(page);
        setData(response.data);
      } catch (error) {
        notifyError(error);
      } finally {
        setLoading(false);
      }
    }
    fetchArticles();
  }, [page, refresh]);

  if (loading)
    return (
      <Center h="100vh">
        <Loader />
      </Center>
    );

  return (
    <Stack p="md">
      {selectedArticleId && (
        <DetailArticleModal
          opened={isModalOpen}
          onClose={() => closeModal()}
          onSuccess={() => setRefresh((r) => r + 1)}
          articleId={selectedArticleId}
          isDoctorView={false}
        />
      )}

      <Group justify="space-between">
        <Title order={2}>Prehľad článkov</Title>
      </Group>

      {data.content.length === 0 ? (
        <Alert bg="yellow">Žiadne články nenájdené</Alert>
      ) : (
        <Card withBorder shadow="sm" p="md">
          {data.content.map((a) => (
            <Card
              key={a.id}
              withBorder
              shadow="sm"
              p="md"
              mb="sm"
              radius="md"
              onClick={() => {
                setSelectedArticleId(a.id);
                openModal();
              }}
            >
              <Group>
                <Group>
                  <IconFirstAidKit />
                  <Stack>
                    <Title order={6}>{a.title}</Title>
                    <Text size="xs">{a.timeOfCreation}</Text>
                  </Stack>
                </Group>
              </Group>
            </Card>
          ))}

          <Pagination
            justify="flex-end"
            size="sm"
            total={data.totalPages}
            value={page}
            onChange={setPage}
            mt="sm"
          />
        </Card>
      )}
    </Stack>
  );
}
