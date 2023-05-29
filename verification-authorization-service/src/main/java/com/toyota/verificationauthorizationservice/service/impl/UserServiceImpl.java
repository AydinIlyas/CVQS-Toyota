package com.toyota.verificationauthorizationservice.service.impl;

import com.toyota.verificationauthorizationservice.dao.RoleRepository;
import com.toyota.verificationauthorizationservice.dao.UserRepository;
import com.toyota.verificationauthorizationservice.domain.Role;
import com.toyota.verificationauthorizationservice.domain.User;
import com.toyota.verificationauthorizationservice.dto.AuthenticationRequest;
import com.toyota.verificationauthorizationservice.dto.AuthenticationResponse;
import com.toyota.verificationauthorizationservice.dto.RegisterRequest;
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
            if (roleOptional.isPresent()) {
                set.add(roleOptional.get());
            }
        }
        if (set.size() < 1) return null;
        userRepository.save(user);
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
            return authorities.stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        }
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
            User user=optionalUser.get();
            user.setDeleted(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /**
     * @param newUsername
     * @param oldUsername
     * @return
     */
    @Override
    public Boolean updateUsername(String newUsername, String oldUsername) {
        Optional<User> optionalUser=userRepository.findByUsernameAndDeletedIsFalse(oldUsername);

        if(optionalUser.isPresent())
        {
            User user=optionalUser.get();
            user.setUsername(newUsername);
            userRepository.save(user);
            return true;
        }
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
