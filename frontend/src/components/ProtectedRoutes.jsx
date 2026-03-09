import { useContext } from 'react';
import { Navigate } from 'react-router-dom';
import { AuthContext } from '../context/authContextValue';

export default function ProtectedRoute({ children, allowedRoles }) {
  const { user } = useContext(AuthContext);

  if (user === null) {
    return <Navigate to="/login" />;
  }

  //logged in but not have required role
  if (allowedRoles && !allowedRoles.includes(user.role)) {
    return <Navigate to="/forbidden" />;
  }

  return children;
}