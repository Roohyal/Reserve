package com.mathias.reserve.repository;

import com.mathias.reserve.domain.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByEmail(String username);

    boolean existsByEmail(String email);

    Optional<Person> findByResetToken(String token);
}
