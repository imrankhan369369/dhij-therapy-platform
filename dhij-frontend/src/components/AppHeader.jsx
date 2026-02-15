
import { AppBar, Toolbar, Typography, Box, IconButton, Button, Chip } from "@mui/material";
import { LightMode, DarkMode, Logout } from "@mui/icons-material";
import { useContext } from "react";
import { ColorModeContext } from "../theme";
import { Link as RouterLink } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function AppHeader() {
  const { user, logout } = useAuth();
  const { toggleColorMode } = useContext(ColorModeContext);

  const isAdmin = user?.role === "ADMIN";

  return (
    <AppBar position="sticky" color="transparent" elevation={0}
      sx={{ backdropFilter: "blur(10px)", borderBottom: (t) => `1px solid ${t.palette.divider}` }}>
      <Toolbar sx={{ minHeight: 72 }}>
        <Typography variant="h6" sx={{ fontWeight: 800, letterSpacing: 0.2 }}>
          DHij<span style={{ color: "#2563eb" }}>App</span>
        </Typography>

        <Box sx={{ flexGrow: 1 }} />

        {user && (
          <Box sx={{ display: "flex", alignItems: "center", gap: 1.5 }}>
            <Chip label={user.role} color={isAdmin ? "secondary" : "primary"} variant="outlined" />
            <Button component={RouterLink} to="/user" color="inherit">User</Button>
            {isAdmin && <Button component={RouterLink} to="/admin" color="inherit">Admin</Button>}
            <IconButton color="inherit" onClick={toggleColorMode} aria-label="toggle theme">
              {/* show an icon either way; MUI automatically switches with palette.mode but we don't have it here */}
              <DarkMode />
            </IconButton>
            <IconButton color="error" onClick={logout} aria-label="logout">
              <Logout />
            </IconButton>
          </Box>
        )}
      </Toolbar>
    </AppBar>
  );
}
