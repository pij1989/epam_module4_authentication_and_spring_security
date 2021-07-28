package com.epam.esm.model.repository;

import com.epam.esm.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleType(Role.RoleType roleType);
}
