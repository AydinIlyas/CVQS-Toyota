package com.toyota.verificationauthorizationservice.service.abstracts;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Interface for JwtServiceImpl
 */
public interface JwtService {
    /**
     * Generates token
     * @param userDetails User details
     * @return Token
     */
    String generateToken(UserDetails userDetails);
    /**
     * Extracts username
     * @param jwt Token
     * @return Username
     */
    String extractUsername(String jwt);
    /**
     * Extracts token ID
     * @param jwt Token
     * @return Token ID
     */
    String extractTokenId(String jwt);
    /**
     * Checks if token is valid
     * @param jwt Token
     * @param userDetails User information
     * @return boolean: if token is valid or not.
     */
    boolean isTokenValid(String jwt, UserDetails userDetails);
}
