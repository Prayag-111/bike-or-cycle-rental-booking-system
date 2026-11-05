package com.bikerental.service;

import com.bikerental.exception.BookingException;
import com.bikerental.model.Activity;
import com.bikerental.model.Booking;
import com.bikerental.model.Cycle;
import com.bikerental.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.LongSupplier;

/**
 * BookingOperationsImpl.java
 * Phase 4 - Server-Side Integration and Execution
 *
 * Keeps the cycles and bookings in memory.
 *  - Every method is synchronized, so two users clicking "Book" on the same cycle
 *    at the same moment cannot both succeed.
 *  - A booking ends by itself when its hours run out, and the cycle becomes available again.
 *  - The latest events are remembered for the live activity feed.
 */
public class BookingOperationsImpl implements BookingOperations {

    public static final int MAX_HOURS = 12;

    private static final long HOUR_MILLIS = 60L * 60L * 1000L;
    private static final int MAX_ACTIVITY = 8;
    private static final DateTimeFormatter WHEN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final LongSupplier clock;
    private final List<Cycle> cycles = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();
    private final Map<String, Long> endTimes = new HashMap<>();
    private final LinkedList<Activity> activity = new LinkedList<>();
    private final AtomicInteger nextId = new AtomicInteger(1000);

    public BookingOperationsImpl() {
        this(System::currentTimeMillis);
    }

    /** The clock can be replaced, which lets tests move time forward. */
    public BookingOperationsImpl(LongSupplier clock) {
        this.clock = clock;
        cycles.add(new Cycle("C201", "Trek Mountain Explorer", "Mountain", 40.0));
        cycles.add(new Cycle("C202", "Hero Cityline", "Hybrid", 25.0));
        cycles.add(new Cycle("C203", "Firefox Road Runner", "Road", 35.0));
        cycles.add(new Cycle("C204", "Hercules Trailblazer", "Mountain", 45.0));
        cycles.add(new Cycle("C205", "Atlas Urban Glide", "Hybrid", 30.0));
        cycles.add(new Cycle("C206", "Volt Electric Cruiser", "Electric", 60.0));
    }

    @Override
    public synchronized List<Cycle> getAllCycles() {
        releaseFinishedBookings();
        // Return copies, so a page that is being drawn never sees a half-finished change
        List<Cycle> copy = new ArrayList<>();
        for (Cycle original : cycles) {
            Cycle c = new Cycle(original.getCycleId(), original.getCycleName(),
                    original.getType(), original.getRatePerHour());
            c.setAvailable(original.isAvailable());
            copy.add(c);
        }
        return copy;
    }

    @Override
    public synchronized Booking bookCycle(User user, String cycleId, int hours)
            throws BookingException {

        releaseFinishedBookings();

        if (user == null) {
            throw new BookingException("Please log in to book a cycle.");
        }
        if (hours < 1 || hours > MAX_HOURS) {
            throw new BookingException("Choose between 1 and " + MAX_HOURS + " hours.");
        }

        Cycle cycle = findCycle(cycleId);
        if (cycle == null) {
            throw new BookingException("That cycle does not exist.");
        }
        if (!cycle.isAvailable()) {
            throw new BookingException(cycle.getCycleName() + " is already booked.");
        }

        long now = clock.getAsLong();
        String when = LocalDateTime.ofInstant(Instant.ofEpochMilli(now), ZoneId.systemDefault()).format(WHEN);

        cycle.setAvailable(false);
        Booking booking = new Booking("B" + nextId.incrementAndGet(), user, cycle, hours, when);
        bookings.add(booking);
        endTimes.put(booking.getBookingId(), now + hours * HOUR_MILLIS);
        addActivity(now, user.getUsername(),
                "booked " + cycle.getCycleName() + " for " + hours + (hours == 1 ? " hour" : " hours"));
        return booking;
    }

    @Override
    public synchronized void cancelBooking(User user, String bookingId) throws BookingException {

        releaseFinishedBookings();

        if (user == null) {
            throw new BookingException("Please log in to cancel a booking.");
        }

        Iterator<Booking> it = bookings.iterator();
        while (it.hasNext()) {
            Booking booking = it.next();
            if (booking.getBookingId().equals(bookingId)) {
                if (!booking.getUser().getUsername().equals(user.getUsername())) {
                    throw new BookingException("You can only cancel your own bookings.");
                }
                booking.getCycle().setAvailable(true);
                endTimes.remove(booking.getBookingId());
                it.remove();
                addActivity(clock.getAsLong(), user.getUsername(),
                        "cancelled " + booking.getCycle().getCycleName());
                return;
            }
        }
        throw new BookingException("Booking not found.");
    }

    @Override
    public synchronized List<Booking> getBookingsFor(String username) {
        releaseFinishedBookings();
        List<Booking> mine = new ArrayList<>();
        for (Booking booking : bookings) {
            if (booking.getUser().getUsername().equals(username)) {
                mine.add(booking);
            }
        }
        return mine;
    }

    @Override
    public synchronized long getEndTimeMillis(String bookingId) {
        Long end = endTimes.get(bookingId);
        return end == null ? 0L : end;
    }

    @Override
    public synchronized List<Activity> getRecentActivity() {
        releaseFinishedBookings();
        return new ArrayList<>(activity);
    }

    /** Ends every booking whose time has run out and frees its cycle. */
    private void releaseFinishedBookings() {
        long now = clock.getAsLong();
        Iterator<Booking> it = bookings.iterator();
        while (it.hasNext()) {
            Booking booking = it.next();
            Long end = endTimes.get(booking.getBookingId());
            if (end != null && end <= now) {
                booking.getCycle().setAvailable(true);
                endTimes.remove(booking.getBookingId());
                it.remove();
                addActivity(end, booking.getUser().getUsername(),
                        "finished with " + booking.getCycle().getCycleName());
            }
        }
    }

    private void addActivity(long time, String username, String message) {
        activity.addFirst(new Activity(time, username, message));
        while (activity.size() > MAX_ACTIVITY) {
            activity.removeLast();
        }
    }

    private Cycle findCycle(String cycleId) {
        for (Cycle c : cycles) {
            if (c.getCycleId().equals(cycleId)) {
                return c;
            }
        }
        return null;
    }
}
