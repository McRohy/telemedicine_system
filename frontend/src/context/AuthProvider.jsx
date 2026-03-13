import { useState } from 'react';
import api from '../configs/api';
import AuthContext from './AuthContext';

export function AuthProvider({ children }) {
  const [user, setUser] = useState(getInitialUser);

  function getInitialUser() {
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
      return JSON.parse(savedUser);
    }
    return null;
  }

  async function login({ email, password }) {
    const loginRequest = {
      email: email,
      password: password,
    };

    const response = await api.post('/auth/login', loginRequest);
    const data = response.data;

    const userData = {
      firstName: data.firstName,
      lastName: data.lastName,
      role: data.role,
      identificationNumber: data.identificationNumber,
    };

    console.log('Login successful, user data:', userData);

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
