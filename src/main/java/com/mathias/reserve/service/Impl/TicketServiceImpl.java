package com.mathias.reserve.service.Impl;

import com.mathias.reserve.domain.entities.*;
import com.mathias.reserve.domain.enums.Status;
import com.mathias.reserve.exceptions.NotFoundException;
import com.mathias.reserve.payload.request.*;
import com.mathias.reserve.payload.response.TicketResponse;
import com.mathias.reserve.repository.*;
import com.mathias.reserve.service.EmailService;
import com.mathias.reserve.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
                .travelDate(ticketDto.getTravelDate())
                .travelTime(ticketDto.getTravelTime())
                .price(ticketDto.getPrice())
                .arrivalTerminal(arrivalTerminal)
                .departureTerminal(departureTerminal)
                .available_tickets(ticketDto.getAvailableTickets())
                .state(state)
                .vehicle(ticketDto.getVehicle())
                .build();

        ticketRepository.save(ticket);

        return TicketResponse.builder()
                .responseCode("003")
                .responseMessage("TICKETS HAVE BEEN CREATED SUCCESSFULLY ")
                .build();
    }

    @Override
    public BookingResponse bookTicket(String email, BookingDto bookingDto) throws Exception {

        // Fetch the ticket
        Ticket ticket = ticketRepository.findById(bookingDto.getTicketId())
                .orElseThrow(() -> new Exception("Ticket not found"));

//         Check ticket availability
        if (ticket.getAvailable_tickets() <= 0) {
            throw new Exception("No tickets available");
        }

        // Fetch the person using their email
        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Person not found"));

        // Create a new booking
        Bookings booking = new Bookings();
        booking.setTicket(ticket);
        booking.setPerson(person);
        booking.setSeatType(bookingDto.getSeatType());
        booking.setBookingNumber(UUID.randomUUID().toString()); // Generate unique booking number
        booking.setStatus(Status.RESERVED);

        // Save the booking
        bookingRepository.save(booking);

        ticket.setAvailable_tickets(ticket.getAvailable_tickets() - 1);
        ticketRepository.save(ticket);

        EmailDetails emailDetails = EmailDetails.builder()
                .fullname(person.getFullName())
                .recipient(person.getEmail())
                .bookingNumber(booking.getBookingNumber())
                .departureTerminal(ticket.getDepartureTerminal())
                .arrivalTerminal(ticket.getArrivalTerminal())
                .seatType(booking.getSeatType())
                .travelTime(ticket.getTravelTime())
                .travelDate(ticket.getTravelDate())
                .subject("RESERVE!!! TICKET BOOKED SUCCESSFULLY ")
                .build();

        emailService.sendEmailAlert(emailDetails,"booking_verification");



        return BookingResponse.builder()
                .responseCode("005")
                .responseMessage("BOOKING VERIFICATION SUCCESSFUL, KINDLY CHECK YOUR EMAIL FOR YOUR DETAILS ")
                .build();
    }
}
