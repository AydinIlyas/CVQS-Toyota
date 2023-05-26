package com.toyota.verificationauthorizationservice.dao;


import com.toyota.verificationauthorizationservice.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String user);
}
