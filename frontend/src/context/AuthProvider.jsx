import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login as loginRequest } from '../api/authApi';
import AuthContext from './AuthContext';

/**
 * AuthProvider is wrapper component that provides the authentication context to all child components.
 */
export function AuthProvider({ children }) {
  const [user, setUser] = useState(getInitialUser);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  /**
   * Loads user data from localStorage on page refresh.
   * This ensures that every refresh does not log the user out.
   */
  function getInitialUser() {
    const savedUser = localStorage.getItem('user');
    return savedUser ? JSON.parse(savedUser) : null;
  }

  /**
   * Authenticates the user with email and password via the backend API.
   * It stores the JWT token and user data in localStorage when successful 
   * and redirects. Otherwise it sets error message.
   */
  async function login({ email, password }) {
    setError(null);
    setLoading(true);
    try {
      const response = await loginRequest({ email, password });
      const data = response.data;

      const userData = {
        firstName: data.firstName,
        lastName: data.lastName,
        role: data.role,
        identificationNumber: data.identificationNumber,
      };

      localStorage.setItem('token', data.token);
      localStorage.setItem('user', JSON.stringify(userData));
      setUser(userData);

      if (userData.role === 'ADMIN') {
        navigate('/admin/doctors');
      } else {
        navigate(`/${userData.role.toLowerCase()}/dashboard`);
      }
    } catch (error) {
      setError(error.response?.data?.message || 'Nastala chyba pri prihlásovaní');
    } finally {
      setLoading(false);
    }
  }

  /**
   * Logs out the user by clearing localStorage and resetting auth state.
   */
  function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
    navigate('/login');
  }

  return (
    <AuthContext.Provider value={{ user, error, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}
