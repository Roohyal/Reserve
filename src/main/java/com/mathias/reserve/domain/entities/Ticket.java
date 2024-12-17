package com.mathias.reserve.domain.entities;

import com.mathias.reserve.domain.enums.SeatType;
import com.mathias.reserve.domain.enums.Status;
import com.mathias.reserve.domain.enums.Vehicle;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket extends BaseEntity {

    private String ticketNo;

    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    private LocalDate travelDate;

    private LocalTime travelTime;

    private int available_tickets;

    private Long price;

    @ManyToOne
    @JoinColumn(name = "departure_terminal_id", nullable = false)
    private Terminal departureTerminal;

    @ManyToOne
    @JoinColumn(name = "arrival_terminal_id", nullable = false)
    private Terminal arrivalTerminal;

    @ManyToOne
    @JoinColumn(name = "state_id", nullable = false)
    private State state;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookings> bookings;


}
