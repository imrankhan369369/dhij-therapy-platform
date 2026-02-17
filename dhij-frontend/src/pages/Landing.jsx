import { useEffect, useState } from "react";
import { Box, Container, Grid, Card, CardMedia, CardContent, Typography, Chip, Button, Stack, CircularProgress, Alert } from "@mui/material";
import { Link as RouterLink } from "react-router-dom";
import { getAllHelpers } from "../services/helpersService";
 
export default function Landing() {
  const [helpers, setHelpers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
 
  useEffect(() => {
    loadHelpers();
  }, []);
 
  const loadHelpers = async () => {
    try {
      setLoading(true);
      const data = await getAllHelpers();
      setHelpers(data);
    } catch (err) {
      setError("Failed to load helpers. Please try again.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };
 
  if (loading) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", minHeight: "60vh" }}>
        <CircularProgress />
      </Box>
    );
  }
 
  return (
    <Box sx={{ py: 6 }}>
      <Container>
        <Stack spacing={1} sx={{ mb: 4 }}>
          <Typography variant="h4" fontWeight={800}>Digital Human Interaction Junction</Typography>
          <Typography color="text.secondary">
            Choose a guide who resonates with you. We're here to help with emotional, spiritual, financial, or general support.
          </Typography>
        </Stack>
 
        {error && <Alert severity="error" sx={{ mb: 3 }}>{error}</Alert>}
 
        <Grid container spacing={3}>
          {helpers.map(h => (
            <Grid item xs={12} sm={6} md={3} key={h.id}>
              <Card sx={{ height: "100%", display: "flex", flexDirection: "column" }}>
                <CardMedia
                  component="img"
                  height="180"
                  image={`/images/${h.name.toLowerCase()}.jpg`}
                  alt={h.name}
                  onError={(e) => { e.currentTarget.src = "/images/placeholder.jpg"; }}
                />
                <CardContent sx={{ flexGrow: 1 }}>
                  <Typography variant="h6" fontWeight={800}>{h.name}</Typography>
                  <Typography variant="body2" color="text.secondary">{h.role}</Typography>
                  <Stack direction="row" spacing={1} sx={{ mt: 1, flexWrap: "wrap", gap: 0.5 }}>
                    <Chip label={h.specialty} size="small" color="primary" />
                  </Stack>
                </CardContent>
                <Button
                  component={RouterLink}
                  to={`/helpers/${h.id}`}
                  variant="contained"
                  sx={{ m: 2 }}
                >
                  View Profile
                </Button>
              </Card>
            </Grid>
          ))}
 
          {helpers.length === 0 && !loading && (
            <Grid item xs={12}>
              <Alert severity="info">No helpers available yet. Please check back later.</Alert>
            </Grid>
          )}
        </Grid>
      </Container>
    </Box>
  );
}
 