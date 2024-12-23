package com.mathias.reserve.payload.request;

import com.mathias.reserve.domain.entities.Terminal;
import com.mathias.reserve.domain.entities.Ticket;
import com.mathias.reserve.domain.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetails {
    private String fullName;

    private String recipient;

    private String messageBody;

    private String subject;

    private String attachment;

    private String link;

    private LocalDate travelDate;

    private LocalTime travelTime;

    private String bookingNumber;

    private SeatType seatType;

    private String arrivalTerminal;

    private String departureTerminal;

    private Long price;

    private String ticketNumber;



}
