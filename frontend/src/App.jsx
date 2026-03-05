import { BrowserRouter, Routes, Route } from "react-router-dom";
import AppLayout from "./layout/AppLayout";
import PasswordPage from "./pages/PasswordPage";
import LoginPage from "./pages/LoginPage";
import PreviewOfDoctors from "./pages/admin/PreviewOfDoctors";
import PreviewOfPatients from "./pages/admin/PreviewOfPatients";
import DoctorPreviewOfPatients from "./pages/doctor/PreviewOfPatients";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<AppLayout />}>
          <Route path="/admin/doctors" element={<PreviewOfDoctors />} />
          <Route path="/admin/patients" element={<PreviewOfPatients />} />
          <Route path="/doctor/patients" element={<DoctorPreviewOfPatients />} />
        </Route>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/password/:token" element={<PasswordPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
