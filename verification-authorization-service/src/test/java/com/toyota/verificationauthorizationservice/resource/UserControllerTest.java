package com.toyota.verificationauthorizationservice.resource;

import com.toyota.verificationauthorizationservice.dto.AuthenticationRequest;
import com.toyota.verificationauthorizationservice.dto.AuthenticationResponse;
import com.toyota.verificationauthorizationservice.dto.PasswordsDTO;
import com.toyota.verificationauthorizationservice.dto.RegisterRequest;
import com.toyota.verificationauthorizationservice.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.swing.text.html.parser.Entity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    private UserController userController;
    @Test
    void register() {
        //given
        RegisterRequest registerRequest=new RegisterRequest();

        //when
        when(userService.register(any(RegisterRequest.class))).thenReturn(true);
        boolean result=userController.register(registerRequest);

        //then
        assertTrue(result);
    }

    @Test
    void login() {
        //given
        AuthenticationRequest request=new AuthenticationRequest();
        AuthenticationResponse expected=new AuthenticationResponse("Token");

        //when
        when(userService.login(any(AuthenticationRequest.class)))
                .thenReturn(expected);
        ResponseEntity<AuthenticationResponse> response=userController.login(request);

        //then
        assertEquals(expected,response.getBody());
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    void update() {
        //given
        String newUsername="new";
        String oldUsername="old";

        //when
        when(userService.updateUsername(anyString(),anyString()))
                .thenReturn(true);
        boolean success= userController.update(newUsername,oldUsername);
        //then
        assertTrue(success);
    }

    @Test
    void addRole() {
        //given
        String username="username";
        String role="role";

        //when
        when(userService.addRole(anyString(),anyString())).thenReturn(true);

        boolean success= userController.addRole(username,role);

        //then
        assertTrue(success);
    }

    @Test
    void removeRole() {
        //given
        String username="username";
        String role="role";

        //when
        when(userService.removeRole(anyString(),anyString())).thenReturn(true);

        boolean success= userController.removeRole(username,role);

        //then
        assertTrue(success);
    }

    @Test
    void changePassword_Success() {
        //given
        HttpServletRequest request=mock(HttpServletRequest.class);
        PasswordsDTO passwordsDTO=new PasswordsDTO();
        //when
        when(userService.changePassword(any(),any())).thenReturn(true);
        ResponseEntity<Entity> response= userController.changePassword(request,passwordsDTO);
        //then
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }
    @Test
    void changePassword_Fail() {
        //given
        HttpServletRequest request=mock(HttpServletRequest.class);
        PasswordsDTO passwordsDTO=new PasswordsDTO();
        //when
        when(userService.changePassword(any(),any())).thenReturn(false);
        ResponseEntity<Entity> response= userController.changePassword(request,passwordsDTO);
        //then
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }

    @Test
    void verify() {
        //given
        HttpServletRequest servletRequest=mock(HttpServletRequest.class);
        Set<String> set=new HashSet<>();
        //when
        when(userService.verify(any())).thenReturn(set);
        Set<String> result=userController.verify(servletRequest);
        //then
        assertEquals(set,result);
    }

    @Test
    void verifyAndUsername() {
        //given
        HttpServletRequest servletRequest=mock(HttpServletRequest.class);
        Map<String,String> map=new HashMap<>();
        //when
        when(userService.verifyAndUsername(any())).thenReturn(map);
        Map<String,String> result=userController.verifyAndUsername(servletRequest);
        //then
        assertEquals(map,result);
    }

    @Test
    void delete() {
        //given
        String username="username";
        //when
        when(userService.delete(anyString())).thenReturn(true);
        boolean result=userController.delete(username);
        //then
        assertTrue(result);
    }
}