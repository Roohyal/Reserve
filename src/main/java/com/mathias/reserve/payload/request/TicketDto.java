package com.mathias.reserve.payload.request;

import com.mathias.reserve.domain.enums.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDto {

    private String ticket_no;

    private LocalDate travelDate;

    private LocalDateTime travelTime;

    private String departureTerminal;

    private String arrivalTerminal;

    private String state;

    private Long departureTerminalId;

    private Long arrivalTerminalId;

    private Long stateId;

    private Vehicle vehicle;

    private int availableTickets;

    private Long price;

}
