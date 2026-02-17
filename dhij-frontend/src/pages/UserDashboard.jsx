
import { useEffect, useState } from "react";
import {
  AppBar, Toolbar, Typography, Container, Box, Stack, Paper, Chip,
  Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton
} from "@mui/material";
import { Logout, Refresh } from "@mui/icons-material";
import { getAllHelpers } from "../services/helpersService";
import { useAuth } from "../auth/AuthContext";
import { useNavigate, Link as RouterLink } from "react-router-dom";

export default function UserDashboard() {
  const { user, logout } = useAuth();
  const [helpers, setHelpers] = useState([]);
  const [loading, setLoading] = useState(false);

  const load = async () => {
    try {
      setLoading(true);
      const list = await getAllHelpers();
      setHelpers(list);
    } catch {
      // Optionally show an alert/snackbar
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  return (
    <Box>
      {/* Header */}
      {/* <AppBar position="sticky" color="transparent" elevation={0}
        sx={{ backdropFilter: "blur(10px)", borderBottom: (t) => `1px solid ${t.palette.divider}` }}>
        <Toolbar sx={{ minHeight: 72 }}>
          <Typography variant="h6" sx={{ fontWeight: 800, letterSpacing: 0.2 }}>
            DHij<span style={{ color: "#2563eb" }}>App</span> — User
          </Typography>
          <Box sx={{ flexGrow: 1 }} />
          <Stack direction="row" spacing={1} alignItems="center">
            <Chip label={`Role: ${user?.role || "-"}`} color="primary" variant="outlined" />
            <IconButton onClick={load} color="primary" title="Refresh">
              <Refresh />
            </IconButton>
            <IconButton onClick={logout} color="error" title="Logout">
              <Logout />
            </IconButton>
          </Stack>
        </Toolbar>
      </AppBar> */}

      {/* Content */}
      <Container sx={{ py: 4 }}>
        <Stack spacing={2} sx={{ mb: 2 }}>
          <Typography variant="h5" fontWeight={800}>User Dashboard</Typography>
          <Typography variant="body2" color="text.secondary">
            Welcome! You can view helpers below.
          </Typography>
        </Stack>

        <Paper sx={{ p: 2 }}>
          <Stack direction="row" alignItems="center" justifyContent="space-between" sx={{ mb: 1 }}>
            <Typography variant="subtitle1" fontWeight={700}>Helpers</Typography>
            <Typography variant="body2" color="text.secondary">
              {loading ? "Loading..." : `${helpers.length} item(s)`}
            </Typography>
          </Stack>

          <TableContainer>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell width="30%">Name</TableCell>
                  <TableCell width="20%">Role</TableCell>
                  <TableCell>Specialty</TableCell>
                </TableRow>
              </TableHead>
<TableBody>
  {helpers.map((h) => (
    <TableRow
      key={h.id}
      hover
      sx={{ cursor: "pointer" }}
      onClick={() => navigate(`/helpers/${h.id}`)}
    >
      <TableCell>
        <Typography
          component={RouterLink}
          to={`/helpers/${h.id}`}
          onClick={(e) => e.stopPropagation()} // avoid row onClick firing too
          style={{ textDecoration: "none", color: "inherit", fontWeight: 600 }}
        >
          {h.name}
        </Typography>
      </TableCell>

      <TableCell>
        <Chip label={h.role} size="small" color="secondary" variant="outlined" />
      </TableCell>

      <TableCell>{h.specialty}</TableCell>
    </TableRow>
  ))}

  {helpers.length === 0 && !loading && (
    <TableRow>
      <TableCell colSpan={3} align="center" sx={{ py: 6, color: "text.secondary" }}>
        No helpers found.
      </TableCell>
    </TableRow>
  )}
</TableBody>

            </Table>
          </TableContainer>
        </Paper>
      </Container>
    </Box>
  );
}
