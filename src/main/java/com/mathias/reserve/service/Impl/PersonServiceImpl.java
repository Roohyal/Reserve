package com.mathias.reserve.service.Impl;

import com.mathias.reserve.domain.entities.ConfirmationToken;
import com.mathias.reserve.domain.entities.JToken;
import com.mathias.reserve.domain.entities.Person;
//import com.mathias.reserve.domain.entities.Role;
import com.mathias.reserve.domain.enums.RoleName;
import com.mathias.reserve.domain.enums.TokenType;
import com.mathias.reserve.exceptions.AlreadyExistException;
import com.mathias.reserve.exceptions.NotEnabledException;
import com.mathias.reserve.exceptions.NotFoundException;
import com.mathias.reserve.infrastucture.config.JwtService;
import com.mathias.reserve.payload.request.*;
import com.mathias.reserve.payload.response.LoginInfo;
import com.mathias.reserve.payload.response.LoginResponse;
import com.mathias.reserve.payload.response.RegisterResponse;
import com.mathias.reserve.repository.ConfirmationTokenRepository;
import com.mathias.reserve.repository.JTokenRepository;
import com.mathias.reserve.repository.PersonRepository;
//import com.mathias.reserve.repository.RoleRepository;
import com.mathias.reserve.service.EmailService;
import com.mathias.reserve.service.PersonService;
import com.mathias.reserve.utils.EmailUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JTokenRepository jTokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final EmailUtil emailUtil;



    @Override
    public RegisterResponse registerPerson(RegisterRequest registerRequest) throws Exception {
        // Validate email format
        String emailRegex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(registerRequest.getEmail());

        if (!matcher.matches()) {
            throw new Exception("Invalid email domain");
        }
        String[] emailParts = registerRequest.getEmail().split("\\.");
        if (emailParts.length < 2 || emailParts[emailParts.length - 1].length() < 2){
            throw new Exception("Invalid email domain");
        }
        if (personRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AlreadyExistException("Email is already in use.");
        }
        if(!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            throw new Exception("Passwords do not match.");
        }

        Optional<Person> existingUser = personRepository.findByEmail(registerRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new AlreadyExistException("User already exists, please Login");
        }

        Person person = Person.builder()
                .fullName(registerRequest.getFullName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .confirmPassword(registerRequest.getConfirmPassword())
                .phone(registerRequest.getPhoneNumber())
                .roleName(RoleName.USER)
                .build();

       Person savedPerson = personRepository.save(person);
        ConfirmationToken confirmationTokenModel = new ConfirmationToken(savedPerson);
        confirmationTokenRepository.save(confirmationTokenModel);

        String confirmationUrl = emailUtil.getVerificationUrl(confirmationTokenModel.getToken());

        EmailDetails emailDetails = EmailDetails.builder()
                .fullName(savedPerson.getFullName())
                .recipient(savedPerson.getEmail())
                .subject("RESERVE!!! ACCOUNT CREATED SUCCESSFULLY")
                .link(confirmationUrl)
                .build();

        emailService.sendEmailAlert(emailDetails,"email_verification");


        return RegisterResponse.builder()
                .responseCode("001")
                .responseMessage("You have been registered successfully, Kindly check your email ")
                .build();
    }
    private void saveUserToken(Person person, String jwtToken){
        var token = JToken.builder()
                .person(person)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        jTokenRepository.save(token);
    }
    private void revokeAllUserTokens(Person person){
        var validUserTokens = jTokenRepository.findAllValidTokenByUser(person.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        jTokenRepository.saveAll(validUserTokens);
    }


    @Override
    public LoginResponse loginPerson(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        Person person = personRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new NotFoundException("User is not found"));

        if (!person.isEnabled()){
            throw new NotEnabledException("User account is not enabled. Please check your email to confirm your account.");
        }

        var jwtToken = jwtService.generateToken(person);
        revokeAllUserTokens(person);
        saveUserToken(person, jwtToken);

        return LoginResponse.builder()
                .responseCode("002")
                .responseMessage("Your have logged in  successfully")
                .loginInfo(LoginInfo.builder()
                        .email(person.getEmail())
                        .token(jwtToken)
                        .build())
                .build();
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest forgetPassword) throws MessagingException {
        Person person = personRepository.findByEmail(forgetPassword.getEmail())
                .orElseThrow(()-> new NotFoundException("User is not found"));

        String token = UUID.randomUUID().toString();
        person.setResetToken(token);
        person.setResetTokenCreationTime(LocalDateTime.now());
        personRepository.save(person);

        String resetUrl = emailUtil.getResetUrl(token);

        EmailDetails emailDetails = EmailDetails.builder()
                .fullName(person.getFullName())
                .recipient(person.getEmail())
                .subject("RESERVE!!!! RESET YOUR PASSWORD ")
                .link(resetUrl)
                .build();

        emailService.sendEmailAlert(emailDetails,"forgot_password");


        return "A reset password link has been sent to your account email address";
    }

    @Override
    public String resetPassword(ResetPasswordRequest resetPasswordRequest) {
        Person person =  personRepository.findByResetToken(resetPasswordRequest.getToken()).orElseThrow(()-> new NotFoundException("User is not found"));

        if (Duration.between(person.getResetTokenCreationTime(), LocalDateTime.now()).toMinutes() > 5) {
            person.setResetToken(null);
            personRepository.save(person);
            throw new NotEnabledException("Token has expired!");
        }
        if(!resetPasswordRequest.getPassword().equals(resetPasswordRequest.getConfirmPassword())){
            throw new NotEnabledException("Confirmation Password does not match!");
        }

        person.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));

        // set the reset token to null
        person.setResetToken(null);

        personRepository.save(person);

        return "Password Reset is Successful";
    }

    @Override
    public String makeAdmin(String email, Long id) {

        personRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // Find the user
        Person person = personRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        // Find the ADMIN role
        Person adminRole = personRepository.findByRoleName(RoleName.ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin role not found"));

        // Set the user's role to ADMIN
        person.setRoleName(RoleName.ADMIN);

        // Save the updated user
        personRepository.save(person);

        return "User with name" + person.getFullName()+ " has been made an admin.";
    }
}
