import {
  AppBar, Toolbar, Typography, Box,
  IconButton, Button, Chip
} from "@mui/material";
import { DarkMode, LightMode, Logout } from "@mui/icons-material";
import { useContext } from "react";
import { ColorModeContext } from "../theme";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
 
export default function AppHeader() {
  const { user, logout } = useAuth();
  const { toggleColorMode } = useContext(ColorModeContext);
  const navigate = useNavigate();
 
  const isAdmin = user?.role === "ADMIN";
 
  return (
    <AppBar
      position="sticky"
      color="transparent"
      elevation={0}
      sx={{
        backdropFilter: "blur(10px)",
        borderBottom: (t) => `1px solid ${t.palette.divider}`,
      }}
    >
      <Toolbar sx={{ minHeight: 72 }}>
        {/* Logo - clicking takes you home */}
        <Typography
          variant="h6"
          sx={{ fontWeight: 800, letterSpacing: 0.2, cursor: "pointer" }}
          onClick={() => navigate("/")}
        >
          DHij<span style={{ color: "#2563eb" }}>App</span>
        </Typography>
 
        <Box sx={{ flexGrow: 1 }} />
 
        {/* If logged in - show user menu */}
        {user ? (
          <Box sx={{ display: "flex", alignItems: "center", gap: 1.5 }}>
            <Chip
              label={user.role}
              color={isAdmin ? "secondary" : "primary"}
              variant="outlined"
            />
            <Button
              component={RouterLink}
              to="/user"
              color="inherit"
            >
              Dashboard
            </Button>
            {isAdmin && (
              <Button
                component={RouterLink}
                to="/admin"
                color="inherit"
              >
                Admin
              </Button>
            )}
            <IconButton
              color="inherit"
              onClick={toggleColorMode}
              aria-label="toggle theme"
            >
              <DarkMode />
            </IconButton>
            <IconButton
              color="error"
              onClick={logout}
              aria-label="logout"
            >
              <Logout />
            </IconButton>
          </Box>
        ) : (
          /* If NOT logged in - show Login/Register buttons */
          <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
            <IconButton
              color="inherit"
              onClick={toggleColorMode}
              aria-label="toggle theme"
            >
              <DarkMode />
            </IconButton>
            <Button
              component={RouterLink}
              to="/login"
              variant="outlined"
              size="small"
            >
              Login
            </Button>
            <Button
              component={RouterLink}
              to="/register"
              variant="contained"
              size="small"
            >
              Register
            </Button>
          </Box>
        )}
      </Toolbar>
    </AppBar>
  );
}
 