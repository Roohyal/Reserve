package com.mathias.reserve.repository;

import com.mathias.reserve.domain.entities.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Bookings, Long> {
}
