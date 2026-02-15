
import React, { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import App from "./App.jsx";
import { AuthProvider } from "./auth/AuthContext.jsx";
import AppThemeProvider from "./theme.jsx";
import "@fontsource-variable/inter/index.css"; // pretty font
import "./styles/main.css"; // keep your existing css if you want

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <AppThemeProvider>
      <AuthProvider>
        <App />
      </AuthProvider>
    </AppThemeProvider>
  </StrictMode>
);
