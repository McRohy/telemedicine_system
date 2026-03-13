import { BrowserRouter, Routes, Route } from 'react-router-dom';
import AppLayout from './configs/AppLayout';
import PasswordPage from './pages/other/PasswordPage';
import LoginPage from './pages/other/LoginPage';
import PreviewOfDoctors from './pages/admin/PreviewOfDoctors';
import PreviewOfPatients from './pages/admin/PreviewOfPatients';
import DoctorPreviewOfPatients from './pages/doctor/PreviewOfPatients';
import PatientDetail from './pages/doctor/PatientDetail';
import Forbidden from './pages/other/Forbidden';
import PreviewOfTypes from './pages/admin/PreviewOfTypes';
import Article from './pages/doctor/Article';
import PatientDashboard from './pages/patient/PatientDashboard';

import ProtectedRoute from './components/ProtectedRoutes';
import { AuthProvider } from './context/AuthProvider';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/" element={<AppLayout />}>
            <Route
              path="/admin/doctors"
              element={
                <ProtectedRoute allowedRoles={['ADMIN']}>
                  <PreviewOfDoctors />
                </ProtectedRoute>
              }
            />
            <Route
              path="/admin/patients"
              element={
                <ProtectedRoute allowedRoles={['ADMIN']}>
                  <PreviewOfPatients />
                </ProtectedRoute>
              }
            />
            <Route
              path="/admin/types-of-measurements"
              element={
                <ProtectedRoute allowedRoles={['ADMIN']}>
                  <PreviewOfTypes />
                </ProtectedRoute>
              }
            />
            <Route
              path="/doctor/patients"
              element={
                <ProtectedRoute allowedRoles={['DOCTOR']}>
                  <DoctorPreviewOfPatients />
                </ProtectedRoute>
              }
            />
            <Route
              path="/doctor/patients/:personalNumber"
              element={
                <ProtectedRoute allowedRoles={['DOCTOR']}>
                  <PatientDetail />
                </ProtectedRoute>
              }
            />
            <Route
              path="/doctor/articles"
              element={
                <ProtectedRoute allowedRoles={['DOCTOR']}>
                  <Article />
                </ProtectedRoute>
              }
            />
            <Route
              path="/patient/dashboard"
              element={
                <ProtectedRoute allowedRoles={['PATIENT']}>
                  <PatientDashboard />
                </ProtectedRoute>
              }
            />
          </Route>
          <Route path="/forbidden" element={<Forbidden />} />
          <Route path="/login" element={<LoginPage />} />
          <Route
            path="/password/:token"
            element={
              <ProtectedRoute>
                <PasswordPage />
              </ProtectedRoute>
            }
          />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
