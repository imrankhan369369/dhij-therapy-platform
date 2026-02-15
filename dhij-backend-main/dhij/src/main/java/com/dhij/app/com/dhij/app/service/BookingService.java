package com.dhij.app.com.dhij.app.service;



import com.dhij.app.com.dhij.app.model.Booking;
import com.dhij.app.com.dhij.app.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private final BookingRepository repo;

    public BookingService(BookingRepository repo) { this.repo = repo; }

    // Working hours 10:00–18:00; session 45 min
    private static final LocalTime START = LocalTime.of(10, 0);
    private static final LocalTime END   = LocalTime.of(18, 0);
    private static final Duration DURATION = Duration.ofMinutes(45);

    public List<LocalDateTime> getAvailableSlots(Long helperId, LocalDate date) {
        LocalDateTime from = date.atTime(START);
        LocalDateTime to   = date.atTime(END);

        // all possible starts
        List<LocalDateTime> allStarts = new ArrayList<>();
        for (LocalDateTime t = from; !t.plus(DURATION).isAfter(to); t = t.plus(DURATION)) {
            allStarts.add(t);
        }

        // booked starts
        List<Booking> bookings = repo.findByHelperIdAndStartTimeBetween(helperId, from, to);
        Set<LocalDateTime> taken = bookings.stream().map(Booking::getStartTime).collect(Collectors.toSet());

        // free = all - taken
        return allStarts.stream().filter(s -> !taken.contains(s)).collect(Collectors.toList());
    }

    public Optional<LocalDateTime> findNextAvailable(Long helperId, LocalDate fromDate) {
        for (int d = 0; d < 7; d++) { // look up to a week ahead
            LocalDate date = fromDate.plusDays(d);
            List<LocalDateTime> free = getAvailableSlots(helperId, date);
            if (!free.isEmpty()) return Optional.of(free.get(0));
        }
        return Optional.empty();
    }

    public Booking book(Long helperId, String username, LocalDateTime start) {
        if (repo.findByHelperIdAndStartTime(helperId, start).isPresent()) {
            throw new IllegalStateException("Slot already booked");
        }
        LocalDateTime end = start.plus(DURATION);
        Booking b = new Booking();
        b.setHelperId(helperId);
        b.setUsername(username);
        b.setStartTime(start);
        b.setEndTime(end);
        return repo.save(b);
    }
}
