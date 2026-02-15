import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login.jsx";
import Landing from "./pages/Landing.jsx";
import HelperProfile from "./pages/HelperProfile.jsx";
import Booking from "./pages/Booking.jsx";
import AdminDashboard from "./pages/AdminDashboard.jsx";
import UserDashboard from "./pages/UserDashboard.jsx";
import ProtectedRoute from "./auth/ProtectedRoute.jsx";
import Register from "./pages/Register.jsx";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public */}
        <Route path="/" element={<Landing />} />
        <Route path="/helpers/:id" element={<HelperProfile />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* ADMIN only */}
        <Route element={<ProtectedRoute allowed={['ADMIN']} />}>
          <Route path="/admin" element={<AdminDashboard />} />
        </Route>

        {/* USER and ADMIN */}
        <Route element={<ProtectedRoute allowed={['USER', 'ADMIN']} />}>
          <Route path="/user" element={<UserDashboard />} />
          <Route path="/book/:id" element={<Booking />} />
        </Route>

        <Route path="/unauthorized" element={<div>Unauthorized</div>} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}