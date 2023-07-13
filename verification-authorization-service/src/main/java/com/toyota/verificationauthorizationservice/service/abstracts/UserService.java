package com.toyota.verificationauthorizationservice.service.abstracts;

import com.toyota.verificationauthorizationservice.dto.AuthenticationRequest;
import com.toyota.verificationauthorizationservice.dto.AuthenticationResponse;
import com.toyota.verificationauthorizationservice.dto.PasswordsDTO;
import com.toyota.verificationauthorizationservice.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * Interface for UserService
 */
public interface UserService {
    /**
     * Checks if a role is assigned and adds user.
     *
     * @param request Register Request
     * @return Token
     */
    Boolean register(RegisterRequest request);
    /**
     * Login
     *
     * @param request Login Request
     * @return Token
     */
    AuthenticationResponse login(AuthenticationRequest request);
    /**
     * Logout
     * @param jwtToken Token
     */
    void logout(String jwtToken);
    /**
     * Soft deletes user
     *
     * @param username Username of user
     * @return Boolean
     */

    Boolean delete(String username);
    /**
     * Updates username
     *
     * @param newUsername New username
     * @param oldUsername Old username
     * @return Boolean
     */

    Boolean updateUsername(String newUsername, String oldUsername);
    /**
     * @param request      HttpServletRequest for extracting username
     * @param passwordsDTO PasswordDTO Which includes new and old password
     * @return boolean
     */

    boolean changePassword(HttpServletRequest request,PasswordsDTO passwordsDTO);
    /**
     * Returns roles and username
     *
     * @return Map with Permissions
     */
    Map<String, String> verify();
    /**
     * Adds new role to user
     *
     * @param username Username of user
     * @param role     New Role which should be added
     * @return boolean
     */
    boolean addRole(String username,String role);
    /**
     * @param username Username of user
     * @param role     Role which should be removed
     * @return boolean
     */
    boolean removeRole(String username, String role);

}
