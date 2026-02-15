
import { useState } from "react";
import { useNavigate, useLocation, Link as RouterLink } from "react-router-dom";
import { loginApi } from "../services/authService";
import { useAuth } from "../auth/AuthContext";
import { jwtDecode } from "jwt-decode";
import {
  Box, Card, CardContent, Typography, TextField, Button, Stack, Alert
} from "@mui/material";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);

  const navigate = useNavigate();
  const location = useLocation();
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSubmitting(true);

    try {
      const { token } = await loginApi({ username, password });

      let decoded = {};
      try { decoded = jwtDecode(token); } catch {}
      const rawRole = decoded?.role || "ROLE_USER";
      const role = rawRole.replace(/^ROLE_+/i, "").toUpperCase();

      login(token, { role });

      const params = new URLSearchParams(location.search);
      const next = params.get("next");
      navigate(next || (role === "ADMIN" ? "/admin" : "/user"), { replace: true });
    } catch {
      setError("Invalid username or password");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Box sx={{
      minHeight: "100dvh",
      display: "grid",
      placeItems: "center",
      background: (t) =>
        t.palette.mode === "light"
          ? "radial-gradient(1200px 600px at 10% -10%, #dbeafe 0%, transparent 40%), radial-gradient(800px 400px at 110% 10%, #fce7f3 0%, transparent 40%), #f4f6f8"
          : "radial-gradient(1200px 600px at 10% -10%, rgba(59,130,246,0.15) 0%, transparent 40%), radial-gradient(800px 400px at 110% 10%, rgba(244,114,182,0.12) 0%, transparent 40%), #0b0f17",
      p: 2,
    }}>
      <Card sx={{ width: 420, maxWidth: "94vw" }}>
        <CardContent sx={{ p: 4 }}>
          <Stack spacing={2}>
            <Typography variant="h5" fontWeight={800} textAlign="center">
              Welcome back
            </Typography>
            <Typography variant="body2" color="text.secondary" textAlign="center">
              Sign in to continue to your dashboard
            </Typography>

            {error && <Alert severity="error">{error}</Alert>}

            <form onSubmit={handleSubmit}>
              <Stack spacing={2}>
                <TextField
                  label="Username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  autoComplete="username"
                  fullWidth
                  required
                />
                <TextField
                  label="Password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  autoComplete="current-password"
                  fullWidth
                  required
                />
                <Button
                  type="submit"
                  variant="contained"
                  size="large"
                  disabled={submitting}
                >
                  {submitting ? "Signing in..." : "Login"}
                </Button>
              </Stack>
            </form>

            <Typography variant="body2" textAlign="center">
              Don&apos;t have an account?{" "}
              <Button component={RouterLink} to="/register" size="small">
                Register
              </Button>
            </Typography>
          </Stack>
        </CardContent>
      </Card>
    </Box>
  );
}

