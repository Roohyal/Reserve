package com.mathias.reserve.payload.request;

import com.mathias.reserve.domain.entities.Terminal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingReport {
    private Long id; // Booking ID
    private String bookingNumber;
    private String seatType;
    private String status;
    private String personName; // Name of the user who made the booking
    private Terminal departureTerminal;
    private Terminal arrivalTerminal;
    private String travelDate;

}
