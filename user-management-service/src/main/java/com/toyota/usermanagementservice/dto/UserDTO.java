package com.toyota.usermanagementservice.dto;


import com.toyota.usermanagementservice.domain.Gender;
import com.toyota.usermanagementservice.domain.Role;
import jakarta.validation.constraints.Email;
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
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    @NotNull
    private String username;
    @Email
    @NotNull
    private String email;
    @NotNull
    @Size(min = 5)
    private String password;
    private Set<Role> role;
    private Gender gender;
}
