package com.toyota.verificationauthorizationservice.service.abstracts;

import com.toyota.verificationauthorizationservice.dto.AuthenticationRequest;
import com.toyota.verificationauthorizationservice.dto.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Set;

public interface UserService {
    AuthenticationResponse register(AuthenticationRequest request);
    AuthenticationResponse login(AuthenticationRequest request);
    Set<String> verify(HttpServletRequest request);
}
