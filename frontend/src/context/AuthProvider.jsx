import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../configs/api';
import AuthContext from './AuthContext';

export function AuthProvider({ children }) {
  const [user, setUser] = useState(getInitialUser);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  //survive page refresh by loading user from localStorage
  //without this, every refresh will logout the user
  function getInitialUser() {
    const savedUser = localStorage.getItem('user');
    return savedUser ? JSON.parse(savedUser) : null;
  }

  async function login({ email, password }) {
    setError(null);
    try {
      const response = await api.post('/auth/login', { email, password });
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
    } catch (err) {
      setError(err.response?.data?.message || 'Nastala chyba pri prihlásovaní');
    }
  }

  function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
    navigate('/login');
  }

  return (
    <AuthContext.Provider value={{ user, error, login, logout }}> 
      {children}
    </AuthContext.Provider>
  );
}