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

    private LocalDate travelDate;

    private LocalDateTime travelTime;

    private Long departureTerminalId;

    private Long arrivalTerminalId;

    private Long stateId;

    private Vehicle vehicle;

    private int availableTickets;

    private Long price;
}
