package com.toyota.usermanagementservice.dao;

import com.toyota.usermanagementservice.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,Long> {
}
