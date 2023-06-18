package com.toyota.verificationauthorizationservice.service.abstracts;

import com.toyota.verificationauthorizationservice.dto.AuthenticationRequest;
import com.toyota.verificationauthorizationservice.dto.AuthenticationResponse;
import com.toyota.verificationauthorizationservice.dto.PasswordsDTO;
import com.toyota.verificationauthorizationservice.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;
import java.util.Set;

/**
 * Interface for UserService
 */
public interface UserService {
    Boolean register(RegisterRequest request);
    AuthenticationResponse login(AuthenticationRequest request);
    Set<String> verify(HttpServletRequest request);

    Boolean delete(String username);

    Boolean updateUsername(String newUsername, String oldUsername);

    boolean changePassword(HttpServletRequest request,PasswordsDTO passwordsDTO);

    Map<String, String> verifyAndUsername(HttpServletRequest request);

    boolean addRole(String username,String role);

    boolean removeRole(String username, String role);
}
