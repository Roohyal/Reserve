package com.mathias.reserve.service;

import com.mathias.reserve.payload.request.BookingDto;
import com.mathias.reserve.payload.request.BookingReport;
import com.mathias.reserve.payload.response.*;
import com.mathias.reserve.payload.request.TerminalDto;
import com.mathias.reserve.payload.request.TicketDto;

import java.util.List;

public interface TicketService {

    String addState (String email, String name);

    String addTerminal(String email, TerminalDto terminalDto);

    TicketResponse createTicket(String email, TicketDto ticketDto);

    BookingResponse bookTicket(String email, BookingDto bookingDto) throws Exception;

    String cancelBooking(String email, Long bookingId);

    List<BookingReport> getCancelledTickets(String email);

    List<BookingReport> getReservedTickets(String email);

    List<TicketDtoResponse> getAllTickets(String email);

    List<PersonResponse> getPeopleByTicketNo (String email, String ticket_no);

    PersonTicketResponse getPersonByBookingNo (String email, String booking_no);

    String deleteBooking(String email, String bookingId);

    String deleteTicket(String email, String ticketId);



}
