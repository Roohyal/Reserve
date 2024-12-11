package com.mathias.reserve.payload.response;

import com.mathias.reserve.domain.enums.Vehicle;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDtoResponse {

    private String ticketNo;

    private LocalDate travelDate;

    private LocalDateTime travelTime;

    private String departureTerminal;

    private String arrivalTerminal;

    private String state;


    private Vehicle vehicle;

    private int availableTickets;

    private Long price;

}
