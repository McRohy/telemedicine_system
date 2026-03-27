import { useState } from 'react';
import { Modal, Stack, TextInput, Button, Textarea } from '@mantine/core';
import { notifySuccess, notifyError } from '../helpers/notificationHelper';
import { createArticle } from '../api/articleApi';
import { useForm } from '@mantine/form';

/** 
 * Modal for creating a new article. 
 * Contains a form with article details and a submit button to add the article.
 */
export default function AddArticle({opened, onClose, onSuccess, doctorPanNumber,}) {
  const [loading, setLoading] = useState(false);
  const formRequest = useForm({
    initialValues: {
      panNumber: doctorPanNumber,
      title: '',
      content: '',
    },
    validate: {
      panNumber: (value) => (value ? null : 'Povinné pole'),
      title: (value) => (value ? null : 'Povinné pole'),
      content: (value) => (value ? null : 'Povinné pole'),
    },
  });

  async function handleCreateArticle() {
    setLoading(true);
    try {
      const res = await createArticle(formRequest.values);
      notifySuccess(
        'Článok pridaný',
        `${res.data.title} - bol úspešne vytvorený: ${res.data.timeOfCreation}.`,
      );
      formRequest.reset();
      onClose();
      onSuccess();
    } catch (error) {
      console.log(error.response);
      const status = error.response?.status;

      if (status === 400) {
        formRequest.setErrors(error.response.data.fieldErrors);
      } else {
        notifyError(error);
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <Modal
      opened={opened}
      onClose={() => {
        formRequest.reset();
        onClose();
      }}
      title="Pridať článok"
      size="lg"
    >
      <form onSubmit={formRequest.onSubmit(handleCreateArticle)}>
        <Stack gap="md">
          <TextInput
            label="Názov článku"
            placeholder="Zadajte názov článku"
            type="text"
            ta="left"
            size="md"
            withAsterisk
            {...formRequest.getInputProps('title')}
          />

          
          <Textarea
            label="Obsah článku"
            placeholder="Zadajte obsah článku"
            ta="left"
            size="md"
            autosize
            minRows={15}
            withAsterisk
            {...formRequest.getInputProps('content')}
          />
           
          <Button type="submit" p="xs" size="md" loading={loading}>
            Pridať článok
          </Button>
        </Stack>
      </form>
    </Modal>
  );
}
