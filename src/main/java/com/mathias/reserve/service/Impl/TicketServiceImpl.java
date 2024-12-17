package com.mathias.reserve.service.Impl;

import com.mathias.reserve.domain.entities.*;
import com.mathias.reserve.domain.enums.Status;
import com.mathias.reserve.exceptions.NotFoundException;
import com.mathias.reserve.payload.request.*;
import com.mathias.reserve.payload.response.*;
import com.mathias.reserve.repository.*;
import com.mathias.reserve.service.EmailService;
import com.mathias.reserve.service.TicketService;
import com.mathias.reserve.utils.AccountUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final PersonRepository personRepository;
    private final StateRepository stateRepository;
    private final TerminalRepository terminalRepository;
    private final BookingRepository bookingRepository;
    private final EmailService emailService;


    @Override
    public String addState(String email, String name) {

        personRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));

        if (stateRepository.existsByName(name)) {
            return "State already exists!";
        }
         State state = new State();
          state.setName(name);
          stateRepository.save(state);

        return "State has been added Successfully!!";
    }

    @Override
    public String addTerminal(String email, TerminalDto terminalDto) {
        personRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));

        State state = stateRepository.findById(terminalDto.getStateId()).orElseThrow(()-> new NotFoundException("State not found"));

        Terminal terminal = Terminal.builder()
                .name(terminalDto.getTerminalName())
                .state(state)
                .build();

         terminalRepository.save(terminal);

        return "Terminal has been added Successfully!!";
    }

    @Override
    public TicketResponse createTicket(String email, TicketDto ticketDto) {

        personRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));

        State state = stateRepository.findById(ticketDto.getStateId())
                .orElseThrow(()-> new NotFoundException("State not found"));

        Terminal arrivalTerminal = terminalRepository.findById(ticketDto.getArrivalTerminalId())
                .orElseThrow(() -> new IllegalArgumentException("Arrival terminal not found"));

        Terminal departureTerminal = terminalRepository.findById(ticketDto.getDepartureTerminalId())
                .orElseThrow(() -> new IllegalArgumentException("Departure terminal not found"));

        if (departureTerminal.equals(arrivalTerminal)) {
            throw new IllegalArgumentException("Departure and arrival terminals cannot be the same");
        }

        Ticket ticket = Ticket.builder()
                .ticketNo(AccountUtil.generateTicketNumber())
                .travelDate(ticketDto.getTravelDate())
                .travelTime(ticketDto.getTravelTime())
                .price(ticketDto.getPrice())
                .arrivalTerminal(arrivalTerminal)
                .departureTerminal(departureTerminal)
                .available_tickets(ticketDto.getAvailableTickets())
                .state(state)
                .seatType(ticketDto.getSeatType())
                .build();

        ticketRepository.save(ticket);

        return TicketResponse.builder()
                .responseCode("003")
                .responseMessage("TICKETS HAVE BEEN CREATED SUCCESSFULLY ")
                .build();
    }

    @Override
    @Transactional
    public BookingResponse bookTicket(String email, BookingDto bookingDto) throws Exception {

        // Fetch the ticket
        Ticket ticket = ticketRepository.findById(bookingDto.getTicketId())
                .orElseThrow(() -> new Exception("Ticket not found"));

        // Check ticket availability
        if (ticket.getAvailable_tickets() <= 0) {
            throw new Exception("No tickets available");
        }

        // Fetch the person using their email
        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Person not found"));

        // Reduce ticket availability (within the transaction)
        ticket.setAvailable_tickets(ticket.getAvailable_tickets() - 1);
        ticketRepository.save(ticket);

        // Create a new booking
        Bookings booking = new Bookings();
        booking.setTicket(ticket);
        booking.setPerson(person);
        booking.setBookingNumber(AccountUtil.generateBookingNumber()); // Generate unique booking number
        booking.setStatus(Status.RESERVED);

        // Save the booking
        try {
            bookingRepository.save(booking);
        } catch (DataIntegrityViolationException e) {
            throw new Exception("Booking could not be completed due to a database constraint", e);
        }

        // Send email
        EmailDetails emailDetails = EmailDetails.builder()
                .fullName(person.getFullName())
                .recipient(person.getEmail())
                .bookingNumber(booking.getBookingNumber())
                .departureTerminal(ticket.getDepartureTerminal().getName())
                .arrivalTerminal(ticket.getArrivalTerminal().getName())
                .seatType(ticket.getSeatType())
                .travelTime(ticket.getTravelTime())
                .travelDate(ticket.getTravelDate())
                .ticketNumber(ticket.getTicketNo())
                .price(ticket.getPrice())
                .subject("RESERVE!!! TICKET BOOKED SUCCESSFULLY")
                .build();

        emailService.sendBookingAlert(emailDetails, "booking_verification");

        return BookingResponse.builder()
                .responseCode("005")
                .responseMessage("BOOKING VERIFICATION SUCCESSFUL, KINDLY CHECK YOUR EMAIL FOR YOUR DETAILS")
                .build();
    }

    @Override
    public String cancelBooking(String email, Long bookingId) {
        personRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));

        // Find the booking for this user
        Bookings booking = bookingRepository.findByIdAndPerson_Email(bookingId, email)
                .orElseThrow(() -> new RuntimeException("Booking not found or not authorized"));

        // Perform cancellation
        booking.setStatus(Status.CANCELLED);
        bookingRepository.save(booking);

        return "Your Ticket has been cancelled Successfully!!";
    }

    @Override
    public List<BookingReport> getCancelledTickets(String email) {
        personRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));

        return bookingRepository.findByStatusAndPersonEmail(Status.CANCELLED, email)
                .stream()
                .map(bookings -> BookingReport.builder()
                        .id(bookings.getId())
                        .bookingNumber(bookings.getBookingNumber())
                        .seatType(bookings.getTicket().getSeatType().name())
                        .status(bookings.getStatus().name())
                        .personName(bookings.getPerson().getFullName())
                        .departureTerminal(bookings.getTicket().getDepartureTerminal())
                        .arrivalTerminal(bookings.getTicket().getArrivalTerminal())

                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingReport> getReservedTickets(String email) {
        personRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));

        return bookingRepository.findByStatusAndPersonEmail(Status.RESERVED, email)
                .stream()
                .map(bookings -> BookingReport.builder()
                        .id(bookings.getId())
                        .bookingNumber(bookings.getBookingNumber())
                        .seatType(bookings.getTicket().getSeatType().name())
                        .status(bookings.getStatus().name())
                        .personName(bookings.getPerson().getFullName())
                        .departureTerminal(bookings.getTicket().getDepartureTerminal())
                        .arrivalTerminal(bookings.getTicket().getArrivalTerminal())

                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDtoResponse> getAllTickets(String email) {
        personRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));

        return ticketRepository.findAll()
                .stream()
                .map(ticket -> TicketDtoResponse.builder()
                        .ticketNo(ticket.getTicketNo())
                        .availableTickets(ticket.getAvailable_tickets())
                        .state(ticket.getState().getName())
                        .travelTime(ticket.getTravelTime())
                        .travelDate(ticket.getTravelDate())
                        .departureTerminal(ticket.getDepartureTerminal().getName())
                        .arrivalTerminal(ticket.getArrivalTerminal().getName())
                        .price(ticket.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<PersonResponse> getPeopleByTicketNo(String email, String ticketNo) {

        personRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));

        Ticket ticket = ticketRepository.findByTicketNo(ticketNo)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ticket_no: " + ticketNo));

        return ticket.getBookings().stream()
                .map(booking -> PersonResponse.builder()
                        .fullName(booking.getPerson().getFullName())
                        .email(booking.getPerson().getEmail())
                        .phone(booking.getPerson().getPhone())
                        .build())
                .collect(Collectors.toList());

    }

    @Override
    public PersonTicketResponse getPersonByBookingNo(String email, String booking_no) {
        personRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));

        Bookings booking = bookingRepository.findByBookingNumber(booking_no)
                .orElseThrow(() -> new RuntimeException("Booking not found with booking number: " + booking_no));

        return PersonTicketResponse.builder()
                .fullName(booking.getPerson().getFullName())
                .email(booking.getPerson().getEmail())
                .arrivalTerminal(booking.getTicket().getArrivalTerminal().getName())
                .departureTerminal(booking.getTicket().getDepartureTerminal().getName())
                .travelDate(booking.getTicket().getTravelDate())
                .travelTime(booking.getTicket().getTravelTime())
                .ticketNumber(booking.getTicket().getTicketNo())
                .seatType(booking.getTicket().getSeatType())
                .build();
    }

    @Override
    public String deleteBooking(String email, String bookingId) {
        personRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));

        Bookings booking = bookingRepository.findByBookingNumber(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with booking number: " + bookingId));

        bookingRepository.delete(booking);
        return "Booking with number " + bookingId + " has been deleted successfully.";
    }

    @Override
    public String deleteTicket(String email, String ticketId) {


        personRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));

        Ticket ticket = ticketRepository.findByTicketNo(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ticket number: " + ticketId));
        ticketRepository.delete(ticket);

        return "Ticket with number " + ticketId + " has been deleted successfully.";
    }
}

