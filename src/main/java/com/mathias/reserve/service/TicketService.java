package com.mathias.reserve.service;

import com.mathias.reserve.payload.request.BookingDto;
import com.mathias.reserve.payload.request.BookingReport;
import com.mathias.reserve.payload.response.BookingResponse;
import com.mathias.reserve.payload.request.TerminalDto;
import com.mathias.reserve.payload.request.TicketDto;
import com.mathias.reserve.payload.response.TicketResponse;

import java.util.List;

public interface TicketService {

    String addState (String email, String name);

    String addTerminal(String email, TerminalDto terminalDto);

    TicketResponse createTicket(String email, TicketDto ticketDto);

    BookingResponse bookTicket(String email, BookingDto bookingDto) throws Exception;

    String cancelBooking(String email, Long bookingId);

    List<BookingReport> getCancelledTickets(String email);

    List<BookingReport> getReservedTickets(String email);


}
