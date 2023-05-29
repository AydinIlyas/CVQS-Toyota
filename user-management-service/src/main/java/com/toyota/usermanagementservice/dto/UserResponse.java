package com.toyota.usermanagementservice.dto;

import com.toyota.usermanagementservice.domain.Gender;
import com.toyota.usermanagementservice.domain.Role;
import lombok.Builder;

import java.util.Set;
@Builder
public class UserResponse {
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private Set<Role> role;
    private Gender gender;

    public UserResponse(String firstname, String lastname, String username, String email, Set<Role> role, Gender gender) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.role = role;
        this.gender = gender;
    }

    public UserResponse() {
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
