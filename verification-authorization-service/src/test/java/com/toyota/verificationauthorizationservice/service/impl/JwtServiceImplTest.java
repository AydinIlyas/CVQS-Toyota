package com.toyota.verificationauthorizationservice.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    @Mock
    private UserDetails userDetails;
    @InjectMocks
    private JwtServiceImpl jwtService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService,"expirationDuration",3600000);
    }

    @Test
    void extractUsername() {
        //given
        String username="testName";
        Mockito.when(userDetails.getUsername())
                .thenReturn(username);
        String token=jwtService.generateToken(userDetails);

        //when
        String result=jwtService.extractUsername(token);

        //then
        assertEquals(username,result);
    }

    @Test
    void generateToken() {
        //given
        String username="testName";
        //when
        Mockito.when(userDetails.getUsername())
                .thenReturn(username);
        String token=jwtService.generateToken(userDetails);

        //then
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token,userDetails));
    }

    @Test
    void isTokenValid() {
        //given
        String username="testUsername";
        Mockito.when(userDetails.getUsername()).thenReturn(username);
        String token=jwtService.generateToken(userDetails);

        //when
        boolean valid=jwtService.isTokenValid(token,userDetails);

        //then
        assertTrue(valid);
    }

    @Test
    void extractTokenId() {
        //given
        MockedStatic<UUID> mockedStatic=Mockito.mockStatic(UUID.class);
        String jti="1234a";
        UUID uuid=mock(UUID.class);
        Mockito.when(UUID.randomUUID())
                .thenReturn(uuid);
        Mockito.when(uuid.toString()).thenReturn(jti);
        String token=jwtService.generateToken(userDetails);

        //when
        String result=jwtService.extractTokenId(token);

        //then
        assertEquals(jti,result);
        mockedStatic.close();
    }
}