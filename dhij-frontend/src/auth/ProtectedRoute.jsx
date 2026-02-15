
import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "./AuthContext";

const ProtectedRoute = ({ allowed }) => {
  const { token, user } = useAuth();

  if (!token) return <Navigate to="/login" replace />;

  if (allowed && user?.role) {
    const role = user.role.toUpperCase();
    const allowedUpper = allowed.map((r) => r.toUpperCase());
    if (!allowedUpper.includes(role)) {
      return <Navigate to="/unauthorized" replace />;
    }
  }

  return <Outlet />;
};

export default ProtectedRoute;
