
import { useState } from "react";
import { useNavigate, Link as RouterLink } from "react-router-dom";
import { registerApi } from "../services/authService";
import {
  Box, Card, CardContent, Typography, TextField, Button, Stack, Alert, MenuItem
} from "@mui/material";

export default function Register() {
  
  const [form, setForm] = useState({ username: "", password: "", role: "USER", email: "" });
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const navigate = useNavigate();

  const onChange = (e) => {
    const { name, value } = e.target;
    setForm((f) => ({ ...f, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSubmitting(true);
    try {
      await registerApi(form); // no auto-login
      navigate("/login", { replace: true });
    } catch (err) {
      // Try to show a meaningful message from the backend
      const status = err?.response?.status;
      const msgFromServer =
        err?.response?.data?.message ||
        err?.response?.data?.error ||
        err?.response?.data?.details ||
        err?.message;

      let msg = "Registration failed, please try again";
      if (status === 409) msg = "Username already exists";
      else if (status === 400) msg = msgFromServer || "Invalid data (400)";
      else if (status === 500) msg = "Server error (500)";
      else if (status === 404) msg = "Endpoint not found (404) — check baseURL/path";
      else if (status === 401 || status === 403) msg = "Not allowed — CORS/authorization issue";

      setError(msg);
      console.error("Register error:", { status, data: err?.response?.data, err });
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
              Create your account
            </Typography>
            <Typography variant="body2" color="text.secondary" textAlign="center">
              Join us and get started in minutes
            </Typography>

            {error && <Alert severity="error">{error}</Alert>}

            <form onSubmit={handleSubmit}>
              <Stack spacing={2}>
                <TextField
                  label="Username"
                  name="username"
                  value={form.username}
                  onChange={onChange}
                  required
                  fullWidth
                />
                <TextField
                  label="Password"
                  name="password"
                  type="password"
                  value={form.password}
                  onChange={onChange}
                  required
                  fullWidth
                />
                <TextField
  label="Email"
  name="email"
  type="email"
  value={form.email}
  onChange={onChange}
  required
  fullWidth
/>
                {/* Keep role as USER for self-registration to avoid accidental admin creation */}
                <TextField
                  select
                  label="Role"
                  name="role"
                  value={form.role}
                  onChange={onChange}
                  fullWidth
                >
                  <MenuItem value="USER">User</MenuItem>
                </TextField>
                <Button type="submit" variant="contained" size="large" disabled={submitting}>
                  {submitting ? "Creating..." : "Register"}
                </Button>
              </Stack>
            </form>

            <Typography variant="body2" textAlign="center">
              Already have an account?{" "}
              <Button component={RouterLink} to="/login" size="small">
                Login
              </Button>
            </Typography>
          </Stack>
        </CardContent>
      </Card>
    </Box>
  );
}
