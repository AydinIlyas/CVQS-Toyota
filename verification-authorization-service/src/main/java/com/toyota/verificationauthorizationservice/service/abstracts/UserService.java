package com.toyota.verificationauthorizationservice.service.abstracts;

import com.toyota.verificationauthorizationservice.dto.AuthenticationRequest;
import com.toyota.verificationauthorizationservice.dto.AuthenticationResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    AuthenticationResponse register(AuthenticationRequest request);
    AuthenticationResponse login(AuthenticationRequest request);
    boolean verify(String authHeader);
}
