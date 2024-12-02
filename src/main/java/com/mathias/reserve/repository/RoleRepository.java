package com.mathias.reserve.repository;

import com.mathias.reserve.domain.entities.Role;
import com.mathias.reserve.domain.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}
