package com.automob.mechanic.service;

import com.automob.mechanic.model.Booking;
import com.automob.mechanic.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking createBooking(Booking booking) {
        booking.setStatus("PENDING");
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAllByOrderByBookedAtDesc();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking updateStatus(Long id, String status) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        List<String> validStatuses = List.of("PENDING", "CONFIRMED", "COMPLETED", "CANCELLED");
        if (!validStatuses.contains(status.toUpperCase()))
            throw new RuntimeException("Invalid status: " + status);
        booking.setStatus(status.toUpperCase());
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id))
            throw new RuntimeException("Booking not found with id: " + id);
        bookingRepository.deleteById(id);
    }

    public List<Booking> searchBookings(String keyword) {
        return bookingRepository.searchByNameOrEmail(keyword);
    }

    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status.toUpperCase());
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total",     bookingRepository.count());
        stats.put("pending",   bookingRepository.countByStatus("PENDING"));
        stats.put("confirmed", bookingRepository.countByStatus("CONFIRMED"));
        stats.put("completed", bookingRepository.countByStatus("COMPLETED"));
        stats.put("cancelled", bookingRepository.countByStatus("CANCELLED"));
        return stats;
    }
}
