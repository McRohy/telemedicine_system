import { BrowserRouter, Routes, Route } from 'react-router-dom';
import AppLayout from './components/AppLayout';
import PasswordPage from './pages/other/PasswordPage';
import LoginPage from './pages/other/LoginPage';
import DoctorsPage from './pages/admin/DoctorsPage';
import PatientsPage from './pages/admin/PatientsPage';
import DoctorDashboard from './pages/doctor/DoctorDashboard';
import PatientDetail from './pages/doctor/PatientDetail';
import Forbidden from './pages/other/Forbidden';
import MeasurementTypesPage from './pages/admin/MeasurementTypesPage';
import DoctorArticles from './pages/doctor/ArticlePage';
import PatientArticles from './pages/patient/ArticlePage';
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
                  <DoctorsPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/admin/patients"
              element={
                <ProtectedRoute allowedRoles={['ADMIN']}>
                  <PatientsPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/admin/types-of-measurements"
              element={
                <ProtectedRoute allowedRoles={['ADMIN']}>
                  <MeasurementTypesPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/doctor/dashboard"
              element={
                <ProtectedRoute allowedRoles={['DOCTOR']}>
                  <DoctorDashboard />
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
                  <DoctorArticles />
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
             <Route
              path="/patient/articles"
              element={
                <ProtectedRoute allowedRoles={['PATIENT']}>
                  <PatientArticles />
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
