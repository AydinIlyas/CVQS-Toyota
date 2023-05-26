package com.toyota.usermanagementservice.dto;


import com.toyota.usermanagementservice.domain.Gender;
import com.toyota.usermanagementservice.domain.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDTO {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String password;
    private Set<Role> role;
    private Gender gender;
}
