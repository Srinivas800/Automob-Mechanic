package com.automob.mechanic.repository;

import com.automob.mechanic.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByStatus(String status);
    List<Booking> findByEmailIgnoreCase(String email);
    long countByStatus(String status);
    List<Booking> findAllByOrderByBookedAtDesc();

    @Query("SELECT b FROM Booking b WHERE " +
           "LOWER(b.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Booking> searchByNameOrEmail(String keyword);
}
