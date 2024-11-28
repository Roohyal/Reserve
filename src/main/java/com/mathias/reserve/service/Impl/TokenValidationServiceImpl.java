package com.mathias.reserve.service.Impl;

import com.mathias.reserve.domain.entities.ConfirmationToken;
import com.mathias.reserve.domain.entities.Person;
import com.mathias.reserve.repository.ConfirmationTokenRepository;
import com.mathias.reserve.repository.PersonRepository;
import com.mathias.reserve.service.TokenValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TokenValidationServiceImpl implements TokenValidationService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PersonRepository personRepository;

    @Override
    public String validateToken(String token) {
        Optional<ConfirmationToken> confirmationTokenOptional = confirmationTokenRepository.findByToken(token);
        if (confirmationTokenOptional.isEmpty()) {
            return "Invalid token";
        }
        ConfirmationToken confirmationToken = confirmationTokenOptional.get();
        if (confirmationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "Token has expired";
        }
        Person person = confirmationToken.getPersons();
        person.setEnabled(true);
        personRepository.save(person);

        confirmationTokenRepository.delete(confirmationToken);

        return "Your Email has been Confirmed Succesfully";
    }
}
