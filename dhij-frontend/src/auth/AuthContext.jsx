
// src/auth/AuthContext.jsx
import { createContext, useContext, useEffect, useState } from "react";
import { setToken, removeToken, getToken } from "../utils/token";
import { jwtDecode } from "jwt-decode";

const AuthContext = createContext(null);

function normalizeRole(raw) {
  if (!raw) return null;
  return raw.replace(/^ROLE_+/i, "").toUpperCase(); 
}

function deriveUserFromToken(token) {
  if (!token) return null;
  try {
    const decoded = jwtDecode(token);
    const role = normalizeRole(decoded?.role || "ROLE_USER");
    return role ? { role } : null;
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const [token, setAuthToken] = useState(getToken());
  const [user, setUser] = useState(deriveUserFromToken(getToken()));

  useEffect(() => {
    setUser(deriveUserFromToken(token));
  }, [token]);

  const login = (jwtToken, userData) => {
    setToken(jwtToken);
    setAuthToken(jwtToken);

    const derived = deriveUserFromToken(jwtToken);
    const passedRole = userData?.role ? normalizeRole(userData.role) : null;
    setUser(passedRole ? { role: passedRole } : derived);
  };

  const logout = () => {
    removeToken();
    setAuthToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ token, user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
