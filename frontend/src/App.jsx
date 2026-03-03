import './index.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import PasswordPage from './pages/PasswordPage';
import LoginPage from './pages/LoginPage';
import PreviewOfDoctors from './pages/PreviewOfDoctors';
import PreviewOfPatients from './pages/PreviewOfPatients';
import PreviewOfMeasurements from './pages/PreviewOfMeasurements';
import DoctorPreviewOfPatients from './pages/DoctorPreviewOfPatients';
import AddDoctor from './pages/AddDoctor';
import AddPatient from './pages/AddPatient';

function App() {
  return (

    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/password/:token" element={<PasswordPage />} />
       
        <Route path="/doctors" element={<PreviewOfDoctors />} />
        <Route path="/patients" element={<PreviewOfPatients />} />
        <Route path="/measurements" element={<PreviewOfMeasurements />} />
        <Route path="/doctor-preview-of-patients" element={<DoctorPreviewOfPatients />} />
        <Route path="/add-doctor" element={<AddDoctor />} />
        <Route path="/add-patient" element={<AddPatient />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App
