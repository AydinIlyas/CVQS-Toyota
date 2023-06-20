package com.toyota.verificationauthorizationservice.dto;

import lombok.*;

/**
 * DTO for token response
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token;
}
