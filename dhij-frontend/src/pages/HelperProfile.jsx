import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Box, Container, Grid, Card, CardMedia, CardContent, Typography,
  Chip, Stack, Tabs, Tab, Button, Divider, CircularProgress, Alert
} from "@mui/material";
import { useAuth } from "../auth/AuthContext";
import { getHelperById } from "../services/helpersService";
 
function TabPanel({ value, index, children }) {
  if (value !== index) return null;
  return <Box sx={{ py: 2 }}>{children}</Box>;
}
 
export default function HelperProfile() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { token } = useAuth();
  const [tab, setTab] = useState(0);
  const [helper, setHelper] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
 
  useEffect(() => {
    loadHelper();
  }, [id]);
 
  const loadHelper = async () => {
    try {
      setLoading(true);
      const data = await getHelperById(id);
      setHelper(data);
    } catch (err) {
      setError("Failed to load helper profile");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };
 
  const onBook = () => {
    if (!token) {
      const next = encodeURIComponent(`/helpers/${id}`);
      navigate(`/login?next=${next}`);
      return;
    }
    navigate(`/book/${id}`);
  };
 
  if (loading) {
    return (
      <Container sx={{ py: 6, display: "flex", justifyContent: "center" }}>
        <CircularProgress />
      </Container>
    );
  }
 
  if (error || !helper) {
    return (
      <Container sx={{ py: 6 }}>
        <Alert severity="error">{error || "Helper not found"}</Alert>
      </Container>
    );
  }
 
  return (
    <Box sx={{ py: 4 }}>
      <Container>
        <Grid container spacing={3}>
          <Grid item xs={12} md={4}>
            <Card>
              <CardMedia
                component="img"
                height="260"
                image={`/images/${helper.name.toLowerCase()}.jpg`}
                alt={helper.name}
                onError={(e) => { e.currentTarget.src = "/images/placeholder.jpg"; }}
              />
              <CardContent>
                <Typography variant="h6" fontWeight={800}>{helper.name}</Typography>
                <Typography variant="body2" color="text.secondary">{helper.role}</Typography>
                <Stack direction="row" spacing={1} sx={{ mt: 1 }}>
                  <Chip label={helper.specialty} size="small" color="primary" />
                </Stack>
                <Divider sx={{ my: 2 }} />
                <Button variant="contained" fullWidth sx={{ mt: 2 }} onClick={onBook}>
                  Book a Session
                </Button>
              </CardContent>
            </Card>
          </Grid>
 
          <Grid item xs={12} md={8}>
            <Card>
              <CardContent>
                <Tabs value={tab} onChange={(e, v) => setTab(v)} variant="scrollable">
                  <Tab label="About" />
                  <Tab label="Role & Specialty" />
                  <Tab label="Availability" />
                </Tabs>
 
                <TabPanel value={tab} index={0}>
                  <Typography>
                    {helper.name} is a {helper.role} specializing in {helper.specialty}.
                    Book a session to get personalized support and guidance.
                  </Typography>
                </TabPanel>
                <TabPanel value={tab} index={1}>
                  <Typography variant="subtitle1" fontWeight={600}>Role:</Typography>
                  <Typography>{helper.role}</Typography>
                  <Typography variant="subtitle1" fontWeight={600} sx={{ mt: 2 }}>Specialty:</Typography>
                  <Typography>{helper.specialty}</Typography>
                </TabPanel>
                <TabPanel value={tab} index={2}>
                  <Typography color="text.secondary">
                    Click "Book a Session" to view available time slots and schedule your appointment.
                  </Typography>
                </TabPanel>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </Container>
    </Box>
  );
}
 