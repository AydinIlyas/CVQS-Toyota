package com.toyota.verificationauthorizationservice.service.impl;

import com.toyota.verificationauthorizationservice.dao.RoleRepository;
import com.toyota.verificationauthorizationservice.dao.UserRepository;
import com.toyota.verificationauthorizationservice.domain.Role;
import com.toyota.verificationauthorizationservice.domain.User;
import com.toyota.verificationauthorizationservice.dto.AuthenticationRequest;
import com.toyota.verificationauthorizationservice.dto.AuthenticationResponse;
import com.toyota.verificationauthorizationservice.service.abstracts.JwtService;
import com.toyota.verificationauthorizationservice.service.abstracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.http.HttpHeaders;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final RoleRepository roleRepository;

    /**
     * @param request
     * @return
     */
    @Override
    public AuthenticationResponse register(AuthenticationRequest request) {
        Optional<Role> role=roleRepository.findByName("USER");
        User user=User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        Set<Role> set=user.getRoles();
        if(set==null)
        {
            set=new HashSet<>();
            user.setRoles(set);
        }
        set.add(role.get());
        userRepository.save(user);
        var jwtToken=jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * @param request
     * @return
     */
    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user=userRepository.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken=jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * @return
     */
    @Override
    public boolean verify(String authHeader) {
        String username=jwtService.extractUsername(authHeader);
        UserDetails userDetails=userDetailsService.loadUserByUsername(username);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        userDetails.getPassword()
                )
        );
        return true;
    }
}
