import './index.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import PasswordPage from './pages/PasswordPage';
import LoginPage from './pages/LoginPage';

function App() {
  return (

    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/password/:token" element={<PasswordPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App
