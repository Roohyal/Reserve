package com.mathias.reserve.payload.request;

import com.mathias.reserve.domain.entities.State;
import com.mathias.reserve.domain.entities.Terminal;
import com.mathias.reserve.domain.enums.SeatType;
import com.mathias.reserve.domain.enums.Vehicle;
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
public class TicketDto {


    private LocalDate travelDate;

    private LocalTime travelTime;

    private Long departureTerminalId;

    private Long arrivalTerminalId;

    private Long stateId;

    private SeatType seatType;

    private int availableTickets;

    private Long price;

}
