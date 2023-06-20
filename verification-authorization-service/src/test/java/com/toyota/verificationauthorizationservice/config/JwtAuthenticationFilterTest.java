package com.toyota.verificationauthorizationservice.config;

import com.toyota.verificationauthorizationservice.service.abstracts.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetails userDetails;
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Test
    void doFilterInternal() throws ServletException, IOException {
        //given
        HttpServletRequest request= Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response=Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain=Mockito.mock(FilterChain.class);
        String jwt="Token";
        String username="username";
        //when
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer Token");
        Mockito.when(jwtService.extractUsername(anyString())).thenReturn(username);
        Mockito.when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        Mockito.when(jwtService.isTokenValid(anyString(),any())).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request,response,filterChain);

        //then
        Mockito.verify(request, times(1)).getHeader("Authorization");
        Mockito.verify(jwtService, times(1)).extractUsername(jwt);
        Mockito.verify(jwtService, times(1)).isTokenValid(jwt, userDetails);
        Mockito.verify(userDetailsService, times(1)).loadUserByUsername(username);
        Mockito.verify(filterChain, times(1)).doFilter(request, response);

    }
    @Test
    void doFilterInternal_InvalidBearer() throws ServletException, IOException {
        //given
        HttpServletRequest request= Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response=Mockito.mock(HttpServletResponse.class);
        FilterChain filterChain=Mockito.mock(FilterChain.class);
        //when
        Mockito.when(request.getHeader("Authorization")).thenReturn("Token");

        jwtAuthenticationFilter.doFilterInternal(request,response,filterChain);

        //then
        Mockito.verify(filterChain, times(1)).doFilter(request, response);

    }
}