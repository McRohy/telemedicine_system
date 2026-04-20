import { useEffect, useState } from 'react';
import { Group, Stack, Button, Title, Text, Loader, Center, Alert, Card, Pagination } from '@mantine/core';
import AddArticle from '../../components/AddArticleModal';
import { useDisclosure } from '@mantine/hooks';
import { IconFirstAidKit } from '@tabler/icons-react';
import { useAuth } from '../../context/AuthContext';
import { notifyError } from '../../helpers/notificationHelper';
import { getArticlesByPanNumber } from '../../api/articleApi';
import DetailArticleModal from '../../components/DetailArticleModal';

/**
 * Doctor page for managing articles.
 * Contains paginated table of articles and button to open the AddArticleModal.
 */
export default function ArticlePage() {
  const { user } = useAuth();
  const actualDoctorPanNumber = user?.identificationNumber;

  const [isModalOpen, { open: openModal, close: closeModal }] = useDisclosure(false);
  const [ isDetailModalOpen, { open: openDetailModal, close: closeDetailModal }] = useDisclosure(false);
  const [selectedArticleId, setSelectedArticleId] = useState(null);

  const [loading, setLoading] = useState(true);
  const [refresh, setRefresh] = useState(0);

  const [page, setPage] = useState(1);
  const [data, setData] = useState({ content: [], totalPages: 0 });

  useEffect(() => {
    async function fetchArticles() {
      try {
        const response = await getArticlesByPanNumber(
          actualDoctorPanNumber,
          page,
        );
        setData(response.data);
      } catch (error) {
        notifyError(error);
      } finally {
        setLoading(false);
      }
    }
    fetchArticles();
  }, [actualDoctorPanNumber, page, refresh]);

  if (loading)
    return (
      <Center h="100vh">
        <Loader />
      </Center>
    );

  return (
    <Stack p="md">
      <AddArticle
        opened={isModalOpen}
        onClose={() => closeModal()}
        onSuccess={() => setRefresh((r) => r + 1)}
        doctorPanNumber={actualDoctorPanNumber}
      />
      {selectedArticleId && (
        <DetailArticleModal
          opened={isDetailModalOpen}
          onClose={() => closeDetailModal()}
          onSuccess={() => setRefresh((r) => r + 1)}
          articleId={selectedArticleId}
          isDoctorView={true}
        />
      )}

      <Group justify="space-between">
        <Title order={2}>Prehľad článkov</Title>
        <Button p="xs" onClick={() => openModal()}>
          Pridať článok
        </Button>
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
                openDetailModal();
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
