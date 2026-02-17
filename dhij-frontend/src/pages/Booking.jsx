import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Box, Container, Card, CardContent, Typography, Button, Stack, Chip, Alert, CircularProgress } from "@mui/material";
import dayjs from "dayjs";
import { getSlots, bookSlot } from "../services/helpersService";
import { getHelperById } from "../services/helpersService";
import { useAuth } from "../auth/AuthContext";
 
export default function Booking() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { token } = useAuth();
 
  const [helper, setHelper] = useState(null);
  const [date, setDate] = useState(dayjs().format("YYYY-MM-DD"));
  const [slots, setSlots] = useState([]);
  const [loading, setLoading] = useState(false);
  const [info, setInfo] = useState("");
  const [error, setError] = useState("");
 
  useEffect(() => {
    if (!token) {
      const next = encodeURIComponent(`/book/${id}`);
      navigate(`/login?next=${next}`, { replace: true });
    } else {
      loadHelper();
      loadSlots();
    }
  }, [token, id, navigate]);
 
  useEffect(() => {
    if (token) loadSlots();
  }, [date]);
 
  const loadHelper = async () => {
    try {
      const data = await getHelperById(id);
      setHelper(data);
    } catch (err) {
      setError("Failed to load helper info");
    }
  };
 
  const loadSlots = async () => {
    setLoading(true);
    setInfo("");
    setError("");
    try {
      const s = await getSlots(id, date);
      setSlots(s);
      if (s.length === 0) setInfo("No slots available for this date. Try another date.");
    } catch (e) {
      setError("Failed to load slots");
    } finally {
      setLoading(false);
    }
  };
 
  const onBook = async (startIso) => {
    setError("");
    setInfo("");
    try {
      await bookSlot(id, startIso);
      setInfo("✅ Booking confirmed! Check your email for details.");
      setTimeout(() => navigate("/user"), 2000);
    } catch (e) {
      const status = e?.response?.status;
      const nextAvailable = e?.response?.data?.nextAvailable;
      if (status === 409 && nextAvailable) {
        setError(`Slot no longer available. Next: ${dayjs(nextAvailable).format("ddd, DD MMM HH:mm")}`);
      } else if (status === 401) {
        navigate(`/login?next=${encodeURIComponent(`/book/${id}`)}`, { replace: true });
      } else {
        setError("Booking failed. Please try another slot.");
      }
    }
  };
 
  if (!helper && !error) {
    return (
      <Container sx={{ py: 6, display: "flex", justifyContent: "center" }}>
        <CircularProgress />
      </Container>
    );
  }
 
  if (error && !helper) {
    return (
      <Container sx={{ py: 6 }}>
        <Alert severity="error">{error}</Alert>
      </Container>
    );
  }
 
  return (
    <Box sx={{ py: 4 }}>
      <Container maxWidth="md">
        <Card>
          <CardContent>
            <Typography variant="h5" fontWeight={800}>
              Book a Session with {helper?.name}
            </Typography>
            <Typography color="text.secondary" sx={{ mt: 0.5 }}>
              {helper?.role} • {helper?.specialty}
            </Typography>
 
            <Stack direction="row" spacing={1.5} sx={{ mt: 3, alignItems: "center" }}>
              <Typography fontWeight={600}>Select Date:</Typography>
              <input
                type="date"
                value={date}
                onChange={(e) => setDate(e.target.value)}
                min={dayjs().format("YYYY-MM-DD")}
                style={{
                  padding: "8px 12px",
                  borderRadius: 8,
                  border: "1px solid #ccc",
                  fontSize: 15,
                }}
              />
              <Button onClick={loadSlots} disabled={loading} variant="outlined" size="small">
                Refresh
              </Button>
            </Stack>
 
            {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
            {info && <Alert severity="success" sx={{ mt: 2 }}>{info}</Alert>}
 
            {loading && (
              <Box sx={{ display: "flex", justifyContent: "center", my: 4 }}>
                <CircularProgress />
              </Box>
            )}
 
            {!loading && slots.length > 0 && (
              <>
                <Typography variant="subtitle1" fontWeight={600} sx={{ mt: 3, mb: 1 }}>
                  Available Slots:
                </Typography>
                <Stack direction="row" spacing={1} sx={{ flexWrap: "wrap", gap: 1 }}>
                  {slots.map((s) => (
                    <Chip
                      key={s}
                      label={dayjs(s).format("h:mm A")}
                      clickable
                      color="primary"
                      onClick={() => onBook(s)}
                      sx={{ fontWeight: 600, fontSize: 14 }}
                    />
                  ))}
                </Stack>
              </>
            )}
 
            <Button
              variant="outlined"
              sx={{ mt: 3 }}
              onClick={() => navigate(`/helpers/${id}`)}
            >
              ← Back to Profile
            </Button>
          </CardContent>
        </Card>
      </Container>
    </Box>
  );
}
 