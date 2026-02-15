
import { useEffect, useState } from "react";
import {
  AppBar, Toolbar, Typography, Container, Box, Stack, Paper, TextField, Button, Alert,
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton, Chip, Divider
} from "@mui/material";
import { Add, Edit as EditIcon, Delete as DeleteIcon, Logout, Refresh, Save, Cancel } from "@mui/icons-material";
import { getAllHelpers, addHelper, deleteHelper, updateHelper } from "../services/helpersService";
import { useAuth } from "../auth/AuthContext";

export default function AdminDashboard() {
  const { user, logout } = useAuth();
  const [helpers, setHelpers] = useState([]);
  const [form, setForm] = useState({ name: "", role: "", specialty: "" });
  const [editing, setEditing] = useState(null);
  const [error, setError] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [loading, setLoading] = useState(false);

  const load = async () => {
    try {
      setLoading(true);
      setError("");
      const list = await getAllHelpers();
      setHelpers(list);
    } catch {
      setError("Failed to load helpers");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const onAdd = async (e) => {
    e.preventDefault();
    setError("");
    setSubmitting(true);
    try {
      await addHelper(form);
      setForm({ name: "", role: "", specialty: "" });
      await load();
    } catch {
      setError("Create failed (ADMIN only).");
    } finally {
      setSubmitting(false);
    }
  };

  const onDelete = async (id) => {
    setError("");
    setSubmitting(true);
    try {
      await deleteHelper(id);
      await load();
    } catch {
      setError("Delete failed (ADMIN only).");
    } finally {
      setSubmitting(false);
    }
  };

  const onUpdate = async (e) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await updateHelper(editing.id, {
        name: editing.name,
        role: editing.role,
        specialty: editing.specialty,
      });
      setEditing(null);
      await load();
    } catch {
      setError("Update failed (ADMIN only).");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <Box>
      {/* Header */}
      <AppBar position="sticky" color="transparent" elevation={0}
        sx={{ backdropFilter: "blur(10px)", borderBottom: (t) => `1px solid ${t.palette.divider}` }}>
        <Toolbar sx={{ minHeight: 72 }}>
          <Typography variant="h6" sx={{ fontWeight: 800, letterSpacing: 0.2 }}>
            DHij<span style={{ color: "#2563eb" }}>App</span> — Admin
          </Typography>
          <Box sx={{ flexGrow: 1 }} />
          <Stack direction="row" spacing={1} alignItems="center">
            <Chip label={`Role: ${user?.role || "-"}`} color="secondary" variant="outlined" />
            <IconButton onClick={load} color="primary" title="Refresh">
              <Refresh />
            </IconButton>
            <IconButton onClick={logout} color="error" title="Logout">
              <Logout />
            </IconButton>
          </Stack>
        </Toolbar>
      </AppBar>

      {/* Content */}
      <Container sx={{ py: 4 }}>
        <Stack spacing={3}>
          <Box>
            <Typography variant="h5" fontWeight={800}>Admin Dashboard</Typography>
            <Typography variant="body2" color="text.secondary">
              Create, edit, or delete helpers. (ADMIN only)
            </Typography>
          </Box>

          {error && <Alert severity="error">{error}</Alert>}

          {/* Create Helper */}
          <Paper sx={{ p: 2 }}>
            <Typography variant="subtitle1" fontWeight={700} sx={{ mb: 1.5 }}>
              Create Helper
            </Typography>
            <Box component="form" onSubmit={onAdd}>
              <Stack direction={{ xs: "column", sm: "row" }} spacing={2}>
                <TextField
                  label="Name"
                  value={form.name}
                  onChange={(e) => setForm({ ...form, name: e.target.value })}
                  required
                  fullWidth
                />
                <TextField
                  label="Role"
                  value={form.role}
                  onChange={(e) => setForm({ ...form, role: e.target.value })}
                  required
                  fullWidth
                />
                <TextField
                  label="Specialty"
                  value={form.specialty}
                  onChange={(e) => setForm({ ...form, specialty: e.target.value })}
                  required
                  fullWidth
                />
                <Button
                  type="submit"
                  variant="contained"
                  startIcon={<Add />}
                  disabled={submitting}
                  sx={{ minWidth: 140 }}
                >
                  {submitting ? "Adding..." : "Add"}
                </Button>
              </Stack>
            </Box>
          </Paper>

          {/* Edit Helper (inline card) */}
          {editing && (
            <Paper sx={{ p: 2 }}>
              <Typography variant="subtitle1" fontWeight={700} sx={{ mb: 1.5 }}>
                Edit Helper
              </Typography>
              <Box component="form" onSubmit={onUpdate}>
                <Stack direction={{ xs: "column", sm: "row" }} spacing={2} sx={{ mb: 2 }}>
                  <TextField
                    label="Name"
                    value={editing.name}
                    onChange={(e) => setEditing({ ...editing, name: e.target.value })}
                    fullWidth
                    required
                  />
                  <TextField
                    label="Role"
                    value={editing.role}
                    onChange={(e) => setEditing({ ...editing, role: e.target.value })}
                    fullWidth
                    required
                  />
                  <TextField
                    label="Specialty"
                    value={editing.specialty}
                    onChange={(e) => setEditing({ ...editing, specialty: e.target.value })}
                    fullWidth
                    required
                  />
                </Stack>
                <Stack direction="row" spacing={1.5}>
                  <Button type="submit" variant="contained" startIcon={<Save />} disabled={submitting}>
                    {submitting ? "Saving..." : "Save"}
                  </Button>
                  <Button variant="outlined" color="inherit" startIcon={<Cancel />} onClick={() => setEditing(null)}>
                    Cancel
                  </Button>
                </Stack>
              </Box>
            </Paper>
          )}

          {/* Helpers Table */}
          <Paper sx={{ p: 2 }}>
            <Stack direction="row" alignItems="center" justifyContent="space-between" sx={{ mb: 1 }}>
              <Typography variant="subtitle1" fontWeight={700}>Helpers</Typography>
              <Typography variant="body2" color="text.secondary">
                {loading ? "Loading..." : `${helpers.length} item(s)`}
              </Typography>
            </Stack>
            <Divider sx={{ mb: 2 }} />
            <TableContainer>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell width="30%">Name</TableCell>
                    <TableCell width="20%">Role</TableCell>
                    <TableCell>Specialty</TableCell>
                    <TableCell align="right" width="160">Actions</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {helpers.map((h) => (
                    <TableRow key={h.id} hover>
                      <TableCell>{h.name}</TableCell>
                      <TableCell>
                        <Chip label={h.role} size="small" color="secondary" variant="outlined" />
                      </TableCell>
                      <TableCell>{h.specialty}</TableCell>
                      <TableCell align="right">
                        <Stack direction="row" spacing={1} justifyContent="flex-end">
                          <IconButton color="primary" size="small" onClick={() => setEditing(h)} title="Edit">
                            <EditIcon />
                          </IconButton>
                          <IconButton color="error" size="small" onClick={() => onDelete(h.id)} title="Delete">
                            <DeleteIcon />
                          </IconButton>
                        </Stack>
                      </TableCell>
                    </TableRow>
                  ))}
                  {helpers.length === 0 && !loading && (
                    <TableRow>
                      <TableCell colSpan={4} align="center" sx={{ py: 6, color: "text.secondary" }}>
                        No helpers found.
                      </TableCell>
                    </TableRow>
                  )}
                </TableBody>
              </Table>
            </TableContainer>
          </Paper>
        </Stack>
      </Container>
    </Box>
  );
}
