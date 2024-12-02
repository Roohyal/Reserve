package com.mathias.reserve.repository;

import com.mathias.reserve.domain.entities.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerminalRepository extends JpaRepository<Terminal, Long> {
}
