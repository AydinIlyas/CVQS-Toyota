package com.toyota.verificationauthorizationservice.dao;


import com.toyota.verificationauthorizationservice.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for accessing user roles in database.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String user);
}
