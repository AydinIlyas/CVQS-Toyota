package com.toyota.verificationauthorizationservice.service.impl;

import com.toyota.verificationauthorizationservice.dao.RoleRepository;
import com.toyota.verificationauthorizationservice.dao.UserRepository;
import com.toyota.verificationauthorizationservice.domain.Role;
import com.toyota.verificationauthorizationservice.domain.User;
import com.toyota.verificationauthorizationservice.dto.AuthenticationRequest;
import com.toyota.verificationauthorizationservice.dto.AuthenticationResponse;
import com.toyota.verificationauthorizationservice.dto.PasswordsDTO;
import com.toyota.verificationauthorizationservice.dto.RegisterRequest;
import com.toyota.verificationauthorizationservice.exception.IncorrectPasswordException;
import com.toyota.verificationauthorizationservice.exception.NoRolesException;
import com.toyota.verificationauthorizationservice.service.abstracts.JwtService;
import com.toyota.verificationauthorizationservice.service.abstracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    /**
     * Checks if a role is assigned and adds user.
     *
     * @param request Register Request
     * @return Token
     */
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (request.getRoles().size() < 1) {
            logger.warn("NO ROLE FOUND FOR REGISTRATION!");
            throw new NoRolesException("NO ROLE FOUND FOR REGISTRATION");
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        Set<Role> set = new HashSet<>();
        user.setRoles(set);
        for (String role : request.getRoles()) {
            Optional<Role> roleOptional = roleRepository.findByName(role);
            roleOptional.ifPresent(set::add);
        }
        if (set.size() < 1) {
            logger.warn("No Valid Roles Found!");
            throw new NoRolesException("No Valid Roles Found!");
        }
        userRepository.save(user);
        logger.info("Successfully created new User! Username: {}",user.getUsername());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    /**
     * Login
     *
     * @param request Login Request
     * @return Token
     */
    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByUsernameAndDeletedIsFalse(request.getUsername()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        logger.info("Successfully Authenticated! Username: {}",user.getUsername());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    /**
     * Verification
     *
     * @param request HttpServletRequest
     * @return Set<String> permissions
     */
    @Override
    public Set<String> verify(HttpServletRequest request) {
        String authHeader = extractToken(request);
        String username = jwtService.extractUsername(authHeader);
        Optional<User> optionalUser = userRepository.findByUsernameAndDeletedIsFalse(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            logger.info("Successfully got authorities for authorization!");
            return authorities.stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        }
        logger.warn("User not found for authorization! Username: {}",username);
        return null;
    }

    /**
     * Soft deletes user
     *
     * @param username Username of user
     * @return Boolean
     */
    @Override
    public Boolean delete(String username) {
        Optional<User> optionalUser = userRepository.findByUsernameAndDeletedIsFalse(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setDeleted(true);
            userRepository.save(user);
            logger.info("Successfully deleted user. {}",user.getUsername());
            return true;
        }
        logger.warn("Delete failed. User not Found!");
        return false;
    }

    /**
     * Updates username
     *
     * @param newUsername New username
     * @param oldUsername Old username
     * @return Boolean
     */
    @Override
    public Boolean updateUsername(String newUsername, String oldUsername) {
        Optional<User> optionalUser = userRepository.findByUsernameAndDeletedIsFalse(oldUsername);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(newUsername);
            userRepository.save(user);
            logger.info("Updated username {} to {}", oldUsername, newUsername);
            return true;
        } else {
            logger.warn("User Not Found!");

        }
        return false;
    }

    /**
     * @param request      HttpServletRequest for extracting username
     * @param passwordsDTO PasswordDTO Which includes new and old password
     * @return boolean
     */
    @Override
    public boolean changePassword(HttpServletRequest request, PasswordsDTO passwordsDTO) {
        String authHeader = extractToken(request);
        if (authHeader == null) return false;
        String username = jwtService.extractUsername(authHeader);

        Optional<User> optionalUser = userRepository.findByUsernameAndDeletedIsFalse(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(passwordsDTO.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(passwordsDTO.getNewPassword()));
                userRepository.save(user);
                logger.info("Password changed successfully!");
                return true;
            } else {
                logger.warn("Incorrect Password!");
                throw new IncorrectPasswordException("Password is incorrect!");
            }
        } else {
            logger.warn("User Not Found!");
            return false;
        }
    }

    /**
     * Returns permissions and username
     * @param request HttpServletRequest request for extracting username
     * @return Map with Permissions
     */
    @Override
    public Map<String, String> verifyAndUsername(HttpServletRequest request) {
        String authHeader = extractToken(request);
        String username = jwtService.extractUsername(authHeader);
        Optional<User> optionalUser = userRepository.findByUsernameAndDeletedIsFalse(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

            Map<String, String> map = authorities.stream()
                    .collect(Collectors.toMap(
                            GrantedAuthority::getAuthority,
                            GrantedAuthority::getAuthority
                    ));
            map.put("Username", username);
            logger.info("User verified and returned with username");
            return map;
        }
        logger.warn("User Not Found!");
        return null;
    }

    /**
     * Adds new role to user
     *
     * @param username Username of user
     * @param role     New Role which should be added
     * @return boolean
     */
    @Override
    public boolean addRole(String username, String role) {
        Optional<User> optionalUser = userRepository.findByUsernameAndDeletedIsFalse(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<Role> foundRole = roleRepository.findByName(role);
            if (foundRole.isPresent()) {
                user.getRoles().add(foundRole.get());
                userRepository.save(user);
                logger.info("ADDED new Role to User. User: {}, Role: {}",user.getUsername(),role);
                return true;
            } else {
                logger.warn("Role not found! Role: {}",role);
                return false;
            }
        }
        logger.warn("User not found!");
        return false;
    }

    /**
     * @param username Username of user
     * @param role     Role which should be removed
     * @return boolean
     */
    @Override
    public boolean removeRole(String username, String role) {
        Optional<User> optionalUser = userRepository.findByUsernameAndDeletedIsFalse(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Optional<Role> foundRole = roleRepository.findByName(role);
            if (foundRole.isPresent()) {
                user.getRoles().remove(foundRole.get());
                userRepository.save(user);
                logger.info("REMOVED role from user! User: {}, Role: {}",user.getUsername(),role);
                return true;
            } else {
                logger.warn("Role not Found! Role: {}",role);
                return false;
            }
        }
        logger.warn("User not Found!");
        return false;
    }


    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

}
