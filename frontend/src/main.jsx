import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App.jsx';
import { createTheme, MantineProvider } from '@mantine/core';
import { generateColors } from '@mantine/colors-generator';
import '@mantine/core/styles.css';
import '@mantine/charts/styles.css';
import '@mantine/dates/styles.css';
import '@mantine/notifications/styles.css';

/**
 * Custom Mantine theme configuration.
 */
const theme = createTheme({
  primaryColor: 'primary',
  autoContrast: true,
  colors: {
    primary: generateColors('#ccedec'),
  },
  components: {
    Modal: {
      defaultProps: {
        centered: true,
        overlayProps: {
          backgroundOpacity: 0.8,
          blur: 5,
          color: '#ccedec',
        },
      },
    },
  },
});

/**
 * Entry point of the React application.
 * Wraps the App component with MantineProvider for theming.
 */
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <MantineProvider theme={theme}>
      <App />
    </MantineProvider>
  </StrictMode>,
);
