package com.toyota.verificationauthorizationservice.dto;

import com.toyota.verificationauthorizationservice.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * DTO for registering User
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private Set<String> roles;
}
