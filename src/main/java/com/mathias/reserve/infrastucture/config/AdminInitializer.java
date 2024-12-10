package com.mathias.reserve.infrastucture.config;

import com.mathias.reserve.domain.entities.Person;

import com.mathias.reserve.domain.enums.RoleName;
import com.mathias.reserve.exceptions.NotFoundException;
import com.mathias.reserve.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class AdminInitializer {
    private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);

    private final PersonRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Value("${qlockin.admin.email}")
    private String adminEmail;

    @Value("${qlockin.admin.password}")
    private String adminPassword;

    @Value("${qlockin.admin.fullname}")
    private String adminFullname;

    // Constructor injection
    public AdminInitializer(PersonRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Bean
    CommandLineRunner initDatabase(PersonRepository personRepository) {
        return args -> {
//            Optional<Person> adminRole = personRepository.findByRoleName(RoleName.ADMIN);
//            if (adminRole.isEmpty()) {
//                throw new NotFoundException("Default role ADMIN not found in the database.");
//            }

            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                Person adminUser = new Person();
                adminUser.setEmail(adminEmail);
                adminUser.setPassword(passwordEncoder.encode(adminPassword));
                adminUser.setRoleName(RoleName.ADMIN); // Set the role directly
                adminUser.setEnabled(true);
                adminUser.setFullName(adminFullname);


                userRepository.save(adminUser);

                logger.info("Admin user seeded into the database.");
            } else {
                logger.info("Admin user already exists.");
            }

        };
    }
}
