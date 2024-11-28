package com.mathias.reserve.infrastucture.controller;

import com.mathias.reserve.payload.request.ForgotPasswordRequest;
import com.mathias.reserve.payload.request.LoginRequest;
import com.mathias.reserve.payload.request.RegisterRequest;
import com.mathias.reserve.payload.request.ResetPasswordRequest;
import com.mathias.reserve.payload.response.LoginResponse;
import com.mathias.reserve.payload.response.RegisterResponse;
import com.mathias.reserve.service.PersonService;
import com.mathias.reserve.service.TokenValidationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final PersonService personService;
    private final TokenValidationService tokenValidationService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest person) throws Exception {

        RegisterResponse response = personService.registerPerson(person);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request){
        LoginResponse response = personService.loginPerson(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmUser(@RequestParam("token") String token){
        String result = tokenValidationService.validateToken(token);
        if ("Email confirmed successfully".equals(result)) {
            return ResponseEntity.ok(Collections.singletonMap("message", result));
        } else {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", result));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest requestDto) throws MessagingException {

        String response = personService.forgotPassword(requestDto);

        return ResponseEntity.ok(response);

    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest requestDto) {
        String response = personService.resetPassword(requestDto);

        return ResponseEntity.ok(response);
    }

}
