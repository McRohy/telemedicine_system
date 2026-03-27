import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login as loginRequest } from '../api/authApi';
import AuthContext from './AuthContext';

export function AuthProvider({ children }) {
  const [user, setUser] = useState(getInitialUser);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  //survive page refresh by loading user from localStorage
  //without this, every refresh will logout the user
  function getInitialUser() {
    try {
      const savedUser = localStorage.getItem('user');
      return savedUser ? JSON.parse(savedUser) : null;
    } catch {
      localStorage.removeItem('user');
      localStorage.removeItem('token');
      return null;
    }
  }

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
