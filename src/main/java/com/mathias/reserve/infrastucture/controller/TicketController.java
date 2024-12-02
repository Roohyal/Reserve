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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
