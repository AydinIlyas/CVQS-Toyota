package com.toyota.verificationauthorizationservice.service.abstracts;

import com.toyota.verificationauthorizationservice.dto.AuthenticationRequest;
import com.toyota.verificationauthorizationservice.dto.AuthenticationResponse;
import com.toyota.verificationauthorizationservice.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Set;

/**
 * Interface for UserService
 */
public interface UserService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse login(AuthenticationRequest request);
    Set<String> verify(HttpServletRequest request);
}
