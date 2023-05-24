package com.toyota.verificationauthorizationservice.dao;


import com.toyota.verificationauthorizationservice.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,Long> {
}
