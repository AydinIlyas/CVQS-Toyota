package com.toyota.verificationauthorizationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO which stores old and new password for updating
 */
public class PasswordsDTO {
    @NotBlank(message="Old password must not be blank")
    String oldPassword;
    @Size(min=8,message = "New password must be at least 8 characters")
    @NotBlank(message = "New password must not be blank")
    String newPassword;

    public PasswordsDTO(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public PasswordsDTO() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
