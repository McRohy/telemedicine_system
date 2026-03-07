import { BrowserRouter, Routes, Route } from "react-router-dom";
import AppLayout from "./configs/AppLayout";
import PasswordPage from "./pages/PasswordPage";
import LoginPage from "./pages/LoginPage";
import PreviewOfDoctors from "./pages/admin/PreviewOfDoctors";
import PreviewOfPatients from "./pages/admin/PreviewOfPatients";
import DoctorPreviewOfPatients from "./pages/doctor/PreviewOfPatients";
import PatientDetail from "./pages/doctor/PatientDetail";
import Unauthorized from "./pages/other/Unauthorized";

import ProtectedRoute from "./components/ProtectedRoutes";
import { AuthProvider } from "./context/AuthContext";

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/" element={<AppLayout />}>
            <Route
              path="/admin/doctors"
              element={
                <ProtectedRoute allowedRoles={["ADMIN"]}>
                  <PreviewOfDoctors />
                </ProtectedRoute>
              }
            />
            <Route
              path="/admin/patients"
              element={
                <ProtectedRoute allowedRoles={["ADMIN"]}>
                  <PreviewOfPatients />
                </ProtectedRoute>
              }
            />
            <Route
              path="/doctor/patients"
              element={
                <ProtectedRoute allowedRoles={["DOCTOR"]}>
                  <DoctorPreviewOfPatients />
                </ProtectedRoute>
              }
            />
            <Route
              path="/doctor/patients/:personalNumber"
              element={<PatientDetail />}
            />
          </Route>
          <Route path="/unauthorized" element={<Unauthorized />} />
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
