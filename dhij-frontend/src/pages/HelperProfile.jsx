
import { useMemo } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Box, Container, Grid, Card, CardMedia, CardContent, Typography,
  Chip, Stack, Tabs, Tab, Button, Divider
} from "@mui/material";
import { useAuth } from "../auth/AuthContext";
import { helpers } from "../data/helpers";
import { useState } from "react";

function TabPanel({ value, index, children }) {
  if (value !== index) return null;
  return <Box sx={{ py: 2 }}>{children}</Box>;
}

export default function HelperProfile() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { token } = useAuth();
  const [tab, setTab] = useState(0);

  const helper = useMemo(
    () => helpers.find((h) => String(h.id) === String(id)),
    [id]
  );

  if (!helper) {
    return (
      <Container sx={{ py: 6 }}>
        <Typography variant="h6">Helper not found.</Typography>
      </Container>
    );
  }

  const onBook = () => {
    // Require login → then go to booking flow
    if (!token) {
      const next = encodeURIComponent(`/helpers/${id}`);
      navigate(`/login?next=${next}`);
      return;
    }
    navigate(`/book/${id}`);
  };

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
                <Stack direction="row" spacing={1} sx={{ mt: 1, flexWrap: "wrap", gap: 0.5 }}>
                  {helper.tags.map((t) => <Chip key={t} label={t} size="small" />)}
                </Stack>
                <Divider sx={{ my: 2 }} />
                <Typography variant="body2" color="text.secondary">Session</Typography>
                <Typography variant="h6" fontWeight={800}>₹{helper.price} / {helper.durationMin} min</Typography>
                <Button variant="contained" sx={{ mt: 2 }} onClick={onBook}>Book a Session</Button>
              </CardContent>
            </Card>
          </Grid>

          <Grid item xs={12} md={8}>
            <Card>
              <CardContent>
                <Tabs value={tab} onChange={(e, v) => setTab(v)} variant="scrollable">
                  <Tab label="About" />
                  <Tab label="Why We’re Here" />
                  <Tab label="What We’ve Done" />
                  <Tab label="Availability" />
                </Tabs>

                <TabPanel value={tab} index={0}>
                  <Typography>{helper.about}</Typography>
                </TabPanel>
                <TabPanel value={tab} index={1}>
                  <Typography>{helper.why}</Typography>
                </TabPanel>
                <TabPanel value={tab} index={2}>
                  <ul style={{ marginTop: 0 }}>
                    {helper.achievements.map((a, idx) => <li key={idx}>{a}</li>)}
                  </ul>
                </TabPanel>
                <TabPanel value={tab} index={3}>
                  <Typography color="text.secondary">
                    Availability coming soon — you’ll be able to choose a time slot here.
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
