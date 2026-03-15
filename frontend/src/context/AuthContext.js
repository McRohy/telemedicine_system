import { createContext, useContext } from 'react';

//https://www.robinwieruch.de/react-router-authentication/ for check how to do it right

const AuthContext = createContext();

export default AuthContext;

export function useAuth() {
  return useContext(AuthContext);
}