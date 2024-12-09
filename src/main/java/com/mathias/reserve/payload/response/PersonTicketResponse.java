package com.mathias.reserve.payload.response;

import com.mathias.reserve.domain.enums.SeatType;
import com.mathias.reserve.domain.enums.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonTicketResponse {
    private String fullName;
    private String email;
    private String ticketNumber;
    private String departureTerminal;
    private String arrivalTerminal;
    private LocalDate travelDate;
    private LocalDateTime travelTime;
    private SeatType seatType;
    public Vehicle vehicle;

}
