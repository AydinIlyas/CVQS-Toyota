package com.toyota.verificationauthorizationservice.dao;


import com.toyota.verificationauthorizationservice.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
