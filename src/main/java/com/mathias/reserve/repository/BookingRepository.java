package com.mathias.reserve.repository;

import com.mathias.reserve.domain.entities.Bookings;
import com.mathias.reserve.domain.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Bookings, Long> {
    Optional<Bookings> findByIdAndPerson_Email(Long bookingId, String email);

    List<Bookings> findByStatus(Status status);
}
