package com.toyota.verificationauthorizationservice.dao;


import com.toyota.verificationauthorizationservice.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for user permissions.
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {
}
