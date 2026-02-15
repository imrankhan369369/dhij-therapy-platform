import { useEffect, useMemo, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Box, Container, Card, CardContent, Typography, Button, Stack, Chip, Alert } from "@mui/material";
import dayjs from "dayjs";
import { getSlots, bookSlot } from "../services/helpersService";
import { helpers } from "../data/helpers";
import { useAuth } from "../auth/AuthContext";

export default function Booking() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { token } = useAuth();

  // 1) If not logged in, redirect to login and then back here
  useEffect(() => {
    if (!token) {
      const next = encodeURIComponent(`/book/${id}`);
      navigate(`/login?next=${next}`, { replace: true });
    }
  }, [token, id, navigate]);

  const helper = useMemo(() => helpers.find(h => String(h.id) === String(id)), [id]);

  const [date, setDate] = useState(dayjs().format("YYYY-MM-DD"));
  const [slots, setSlots] = useState([]);
  const [loading, setLoading] = useState(false);
  const [info, setInfo] = useState("");
  const [error, setError] = useState("");

  const load = async () => {
    setLoading(true); setInfo(""); setError("");
    try {
      const s = await getSlots(id, date);
      setSlots(s); // array of ISO datetime strings
      if (s.length === 0) setInfo("No slots today. Try another date.");
    } catch (e) {
      setError("Failed to load slots.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { if (token) load(); }, [date, id, token]);

  const onBook = async (startIso) => {
    setError(""); setInfo("");
    try {
      await bookSlot(id, startIso);
      setInfo("Booked! Check your email/calendar.");
      // Optionally navigate to /user
    } catch (e) {
      const status = e?.response?.status;
      const nextAvailable = e?.response?.data?.nextAvailable;
      if (status === 409 && nextAvailable) {
        setError(`That slot is no longer available. Next: ${dayjs(nextAvailable).format("ddd, DD MMM HH:mm")}`);
      } else if (status === 401) {
        const next = encodeURIComponent(`/book/${id}`);
        navigate(`/login?next=${next}`, { replace: true });
      } else {
        setError("Booking failed. Please try another slot.");
      }
    }
  };

  if (!helper) {
    return <Container sx={{ py: 6 }}><Typography>Helper not found.</Typography></Container>;
  }

  return (
    <Box sx={{ py: 4 }}>
      <Container>
        <Card>
          <CardContent>
            <Typography variant="h5" fontWeight={800}>
              Book a Session with {helper.name}
            </Typography>
            <Typography color="text.secondary" sx={{ mt: 0.5 }}>
              {helper.durationMin} minutes · ₹{helper.price}
            </Typography>

            <Stack direction="row" spacing={1.5} sx={{ mt: 3, alignItems: "center" }}>
              <Typography>Date:</Typography>
              <input
                type="date"
                value={date}
                onChange={(e) => setDate(e.target.value)}
                style={{ padding: 8, borderRadius: 8, border: "1px solid #ccc" }}
              />
              <Button onClick={load} disabled={loading} variant="outlined">Refresh</Button>
            </Stack>

            {error && <Alert severity="error" sx={{ mt: 2 }}>{error}</Alert>}
            {info && <Alert severity="info" sx={{ mt: 2 }}>{info}</Alert>}

            <Stack direction="row" spacing={1} sx={{ mt: 3, flexWrap: "wrap", gap: 1 }}>
              {slots.map((s) => (
                <Chip
                  key={s}
                  label={dayjs(s).format("HH:mm")}
                  clickable
                  color="primary"
                  onClick={() => onBook(s)}
                  sx={{ fontWeight: 600 }}
                />
              ))}
            </Stack>

            <Button variant="outlined" sx={{ mt: 3 }} onClick={() => navigate(`/helpers/${id}`)}>
              Back to Profile
            </Button>
          </CardContent>
        </Card>
      </Container>
    </Box>
  );
}