import { useState } from 'react';
import api from '../configs/api';
import { AuthContext } from './authContextValue';

export function AuthProvider({ children }) {
  const saved = localStorage.getItem('user');
  const initialUser = saved ? JSON.parse(saved) : null;
  const [user, setUser] = useState(initialUser);

  async function login(credentials) {
    const response = await api.post('/auth/login', credentials);
    const data = response.data;

    const userData = {
      firstName: data.firstName,
      lastName: data.lastName,
      role: data.role,
    };

    localStorage.setItem('token', data.token);
    localStorage.setItem('user', JSON.stringify(userData));
    setUser(userData);
  }

  async function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
  }

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}