package com.toyota.verificationauthorizationservice.dao;

import com.toyota.verificationauthorizationservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for accessing users in database
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
//    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndDeletedIsFalse(String username);
}
