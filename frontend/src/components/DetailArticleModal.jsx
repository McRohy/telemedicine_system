import { useState, useEffect } from 'react';
import { Modal, Stack, Title, Text, Center, Loader, Button } from '@mantine/core';
import { notifyError, notifySuccess } from '../helpers/notificationHelper';
import { deleteArticle, getArticleById } from '../api/articleApi';

export default function DetailArticleModal({ opened, onClose, onSuccess, articleId, isDoctorView}) {
  const [loading, setLoading] = useState(false);
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
    } catch (err) {
      console.log(err.response);
      notifyError(err);
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
      {loading == true ? (
        <Center>
          <Loader />
        </Center>
      ) : (
        <Stack gap="md" p="xs">
          <Title order={3}>{article.title}</Title>
          <Text size="xs">{article.timeOfCreation}</Text>
          <Text align="justify">{article.content}</Text>
          {isDoctorView && (
            <Button
              variant="outline"
              color="red"
              onClick={() => handleDeleteArticle()}
            >
              Odstrániť článok
            </Button>
          )}
        </Stack>
      )}
    </Modal>
  );
}
