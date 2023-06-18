package com.toyota.verificationauthorizationservice.dto;

import lombok.*;

/**
 * DTO for login requests
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest {
    private String username;
    private String password;
}
