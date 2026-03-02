import './index.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import PasswordPage from './pages/PasswordPage';
import LoginPage from './pages/LoginPage';
import PreviewOfDoctors from './pages/PreviewOfDoctors';
import PreviewOfPatients from './pages/PreviewOfPatients';

function App() {
  return (

    <BrowserRouter>
      

      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/password/:token" element={<PasswordPage />} />
       
        <Route path="/doctors" element={<PreviewOfDoctors />} />
        <Route path="/patients" element={<PreviewOfPatients />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App
