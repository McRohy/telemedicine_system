import { createContext, useContext } from 'react';

/**
 * AuthContext provides authentication state across the application.
 * https://www.robinwieruch.de/react-router-authentication/ 
 */
const AuthContext = createContext();

export default AuthContext;


/**
 * Custom hook for accessing authentication context.
 */
export function useAuth() {
  return useContext(AuthContext);
}