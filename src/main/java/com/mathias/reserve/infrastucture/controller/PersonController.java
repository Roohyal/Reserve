package com.mathias.reserve.infrastucture.controller;

import com.mathias.reserve.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class PersonController {
    private final PersonService personService;

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/make-admin")
    public ResponseEntity<?> makeAnAdmin(@RequestParam Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return ResponseEntity.ok(personService.makeAdmin(currentUsername,id));
    }
}
