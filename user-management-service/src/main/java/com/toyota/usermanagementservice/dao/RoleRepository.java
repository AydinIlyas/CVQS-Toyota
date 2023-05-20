package com.toyota.usermanagementservice.dao;

import com.toyota.usermanagementservice.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
