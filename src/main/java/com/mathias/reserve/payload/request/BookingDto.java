package com.mathias.reserve.payload.request;

import com.mathias.reserve.domain.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BookingDto {
    private Long ticketId; // ID of the ticket being booked
    private SeatType seatType; // Chosen seat type (e.g., ECONOMY, BUSINESS, etc.)
}
