
// src/theme.jsx
import { createTheme, ThemeProvider, CssBaseline } from "@mui/material";
import { createContext, useMemo, useState } from "react";

export const ColorModeContext = createContext({ toggleColorMode: () => {} });

export default function AppThemeProvider({ children }) {
  const [mode, setMode] = useState("light");

  const colorMode = useMemo(
    () => ({
      toggleColorMode: () =>
        setMode((prev) => (prev === "light" ? "dark" : "light")),
    }),
    []
  );

  const theme = useMemo(
    () =>
      createTheme({
        palette: {
          mode,
          primary: { main: "#2563eb" }, // brand blue
          secondary: { main: "#22c55e" },
          background: {
            default: mode === "light" ? "#f4f6f8" : "#0b0f17",
            paper: mode === "light" ? "#ffffff" : "#0f1725",
          },
        },
        shape: { borderRadius: 12 },
        typography: {
          fontFamily: '"Inter", system-ui, -apple-system, Segoe UI, Roboto',
        },
        components: {
          MuiButton: {
            styleOverrides: {
              root: { textTransform: "none", fontWeight: 600 },
            },
          },
          MuiCard: {
            styleOverrides: {
              root: { boxShadow: "0 10px 30px rgba(2,12,27,0.06)" },
            },
          },
        },
      }),
    [mode]
  );

  return (
    <ColorModeContext.Provider value={colorMode}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        {children}
      </ThemeProvider>
    </ColorModeContext.Provider>
  );
}
