package com.toyota.verificationauthorizationservice.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Username must not be blank")
    private String username;
    @NotBlank(message = "Password must not be blank")
    private String password;
}
