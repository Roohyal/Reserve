package com.mathias.reserve.infrastucture.controller;

import com.mathias.reserve.payload.request.BookingDto;
import com.mathias.reserve.payload.request.TerminalDto;
import com.mathias.reserve.payload.request.TicketDto;
import com.mathias.reserve.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ticket")
public class TicketController {
    private final TicketService ticketService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add_state")
    public ResponseEntity<?> addState(@RequestParam String name){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return ResponseEntity.ok(ticketService.addState(currentUsername, name));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("add_terminal")
    public ResponseEntity<?> addTerminal(@RequestParam TerminalDto terminalDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return ResponseEntity.ok(ticketService.addTerminal(currentUsername, terminalDto));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create_tickets")
    public ResponseEntity<?> createTickets(@RequestParam TicketDto ticketDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return ResponseEntity.ok(ticketService.createTicket(currentUsername, ticketDto));
    }
    
    @PostMapping("/book-tickets")
    public ResponseEntity<?> bookTickets(@RequestParam BookingDto bookingDto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return ResponseEntity.ok(ticketService.bookTicket(currentUsername,bookingDto));
    }

    @PutMapping("/cancel-ticket")
    public ResponseEntity<?> cancelTicket(@RequestParam Long bookingId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return ResponseEntity.ok(ticketService.cancelBooking(currentUsername,bookingId));
    }


    @GetMapping("/get-cancelled-tickets")
    public ResponseEntity<?> getCancelledTickets() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return ResponseEntity.ok(ticketService.getCancelledTickets(currentUsername));
    }


    @GetMapping("/get-reserved-tickets")
    public ResponseEntity<?> getReservedTickets() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return ResponseEntity.ok(ticketService.getReservedTickets(currentUsername));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all-tickets")
    public ResponseEntity<?> getAllTickets() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return ResponseEntity.ok(ticketService.getAllTickets(currentUsername));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-people-by-ticket-no")
    public ResponseEntity<?> getPeopleByTicketNo(@RequestParam String ticketNo) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return ResponseEntity.ok(ticketService.getPeopleByTicketNo(currentUsername,ticketNo));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-person-by-booking-no")
    public ResponseEntity<?> getPersonByBookingNo(@RequestParam String bookingNo) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return ResponseEntity.ok(ticketService.getPersonByBookingNo(currentUsername,bookingNo));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete-booking")
    public ResponseEntity<?> deleteBooking(@RequestParam String bookingId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return ResponseEntity.ok(ticketService.deleteBooking(currentUsername,bookingId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("delete-ticket")
    public ResponseEntity<?> deleteTicket(@RequestParam String ticketId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return ResponseEntity.ok(ticketService.deleteTicket(currentUsername,ticketId));
    }

}
