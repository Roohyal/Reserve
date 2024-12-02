package com.mathias.reserve.repository;

import com.mathias.reserve.domain.entities.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, Long> {

    boolean existsByName(String name);
}
