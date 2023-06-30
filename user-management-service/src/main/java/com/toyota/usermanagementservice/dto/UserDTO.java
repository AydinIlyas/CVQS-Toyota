package com.toyota.usermanagementservice.dto;


import com.toyota.usermanagementservice.domain.Gender;
import com.toyota.usermanagementservice.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

/**
 * DTO with password field for registering user.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotBlank(message = "Firstname must not be blank")
    private String firstname;
    @NotBlank(message="Lastname must not be blank")
    private String lastname;
    @NotBlank(message = "Username must not be blank")
    private String username;
    @Email(message = "It must be a valid email")
    @NotBlank(message = "Email must no be blank")
    private String email;
    @NotBlank(message = "Password must not be blank")
    @Size(min = 8,message = "Password must have at least 8 characters")
    private String password;
    @Size(min = 1,message = "User must have at least one role")
    @NotNull(message = "Role must not be null")
    private Set<Role> role;
    private Gender gender;
}
