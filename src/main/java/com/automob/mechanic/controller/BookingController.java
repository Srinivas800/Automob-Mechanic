package com.automob.mechanic.controller;

import com.automob.mechanic.model.Booking;
import com.automob.mechanic.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    private Map<String, Object> toMap(Booking b) {
        Map<String, Object> m = new HashMap<>();
        m.put("id",            b.getId());
        m.put("name",          b.getName());
        m.put("email",         b.getEmail());
        m.put("phone",         b.getPhone() != null ? b.getPhone() : "");
        m.put("phoneCode",     b.getPhoneCode() != null ? b.getPhoneCode() : "");
        m.put("service",       b.getService());
        m.put("carModel",      b.getCarModel());
        m.put("fuelType",      b.getFuelType() != null ? b.getFuelType() : "");
        m.put("kilometersRan", b.getKilometersRan() != null ? b.getKilometersRan() : "");
        m.put("bookedAt",      b.getBookedAt() != null ? b.getBookedAt().toString() : "");
        m.put("status",        b.getStatus());
        return m;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createBooking(@Valid @RequestBody Booking booking) {
        Booking saved = bookingService.createBooking(booking);
        Map<String, Object> response = new HashMap<>();
        response.put("success",   true);
        response.put("message",   "Booking confirmed! We will contact you soon.");
        response.put("bookingId", saved.getId());
        response.put("name",      saved.getName());
        response.put("service",   saved.getService());
        response.put("status",    saved.getStatus());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllBookings() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Booking b : bookingService.getAllBookings()) result.add(toMap(b));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBookingById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        Optional<Booking> booking = bookingService.getBookingById(id);
        if (booking.isPresent()) return ResponseEntity.ok(toMap(booking.get()));
        response.put("error", "Booking not found with id: " + id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateStatus(@PathVariable Long id,
                                                             @RequestBody Map<String, String> body) {
        Map<String, Object> response = new HashMap<>();
        try {
            Booking updated = bookingService.updateStatus(id, body.get("status"));
            response.put("success", true);
            response.put("message", "Status updated to " + updated.getStatus());
            response.put("id",      updated.getId());
            response.put("status",  updated.getStatus());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("error",   e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteBooking(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            bookingService.deleteBooking(id);
            response.put("success", true);
            response.put("message", "Booking deleted");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("error",   e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> search(@RequestParam String q) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Booking b : bookingService.searchBookings(q)) result.add(toMap(b));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Map<String, Object>>> getByStatus(@PathVariable String status) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Booking b : bookingService.getBookingsByStatus(status)) result.add(toMap(b));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(bookingService.getDashboardStats());
    }
}
