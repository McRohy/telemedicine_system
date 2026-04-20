import { useEffect, useState } from 'react';
import { Modal, Stack, Title, Text, Center, Loader, Button } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { notifyError, notifySuccess } from '../helpers/notificationHelper';
import { deleteArticle, getArticleById } from '../api/articleApi';
import ConfirmModal from './ConfirmModal';

/** 
 * Modal for displaying article details.
 * Contains also a delete button for doctor.
 */
export default function DetailArticleModal({ opened, onClose, onSuccess, articleId, isDoctorView}) {
  const [loading, setLoading] = useState(false);
  const [isConfirmModalOpen, { open: openModal, close: closeModal }] = useDisclosure(false);
  const [article, setArticle] = useState({
    title: '',
    content: '',
    timeOfCreation: '',
  });

  useEffect(() => {
    async function fetchArticle() {
      setLoading(true);
      try {
        const res = await getArticleById(articleId);
        setArticle(res.data);
      } catch (error) {
        notifyError(error);
      } finally {
        setLoading(false);
      }
    }
    fetchArticle();
  }, [articleId]);

  async function handleDeleteArticle() {
    setLoading(true);
    try {
      await deleteArticle(articleId);
      notifySuccess('Článok odstránený', `Článok bol úspešne odstránený.`);
      onClose();
      onSuccess();
    } catch (error) {
      notifyError(error);
    } finally {
      setLoading(false);
    }
  }

  return (
    <Modal
      opened={opened}
      onClose={() => onClose()}
      title="Detail článku"
      size="lg"
      closeOnClickOutside={false}
    >
      {loading ? (
        <Center>
          <Loader />
        </Center>
      ) : (
        <Stack gap="md" p="xs">
          <Title order={3}>{article.title}</Title>
          <Text size="xs">{article.timeOfCreation}</Text>
          <Text ta="justify">{article.content}</Text>
          {isDoctorView && (
            <Button
              variant="outline"
              color="red"
              onClick={openModal}
            >
              Odstrániť článok
            </Button>
          )}
        </Stack>
      )}

      <ConfirmModal
        opened={isConfirmModalOpen}
        onClose={closeModal}
        onConfirm={handleDeleteArticle}
        title="Potvrdenie odstránenia"
        message="Chcete odstrániť tento článok? Zmeny nie je možné vrátiť späť."
        buttonText="Odstrániť"
      />
    </Modal>
  );
}
