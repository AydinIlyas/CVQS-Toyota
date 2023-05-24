package com.toyota.verificationauthorizationservice.dao;

import com.toyota.verificationauthorizationservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}
