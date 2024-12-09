package com.mathias.reserve.repository;

import com.mathias.reserve.domain.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByTicketNo(String ticketNo);
}
