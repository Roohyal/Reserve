package com.mathias.reserve.domain.entities;

import com.mathias.reserve.domain.enums.SeatType;
import com.mathias.reserve.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
@Builder
public class Bookings extends BaseEntity {


    @Enumerated(EnumType.STRING)
    private SeatType seatType;

    @Column(nullable = false, unique = true)
    private String bookingNumber;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    // Many bookings belong to one person
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
}
