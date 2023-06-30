package com.toyota.usermanagementservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

/**
 * Class for sending request to save user in verification service
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username must not be blank")
    private String username;
    @NotBlank(message = "Password must not be blank")
    @Size(min=8)
    private String password;
    @NotNull(message = "Roles must not be null")
    @Size(min = 1,message = "User must have at least 1 role!")
    private Set<String> roles;
}
