package com.toyota.verificationauthorizationservice.service.impl;

import com.toyota.verificationauthorizationservice.dao.RoleRepository;
import com.toyota.verificationauthorizationservice.dao.UserRepository;
import com.toyota.verificationauthorizationservice.domain.Permission;
import com.toyota.verificationauthorizationservice.domain.Role;
import com.toyota.verificationauthorizationservice.domain.User;
import com.toyota.verificationauthorizationservice.dto.AuthenticationRequest;
import com.toyota.verificationauthorizationservice.dto.AuthenticationResponse;
import com.toyota.verificationauthorizationservice.dto.PasswordsDTO;
import com.toyota.verificationauthorizationservice.dto.RegisterRequest;
import com.toyota.verificationauthorizationservice.exception.*;
import com.toyota.verificationauthorizationservice.service.abstracts.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Test
    void register_success() {
        //given
        RegisterRequest registerRequest=new RegisterRequest("Ahmet","test",Set.of(""));
        Role existingRole=new Role();

        //when
        when(passwordEncoder.encode(anyString())).thenReturn("...");
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(existingRole));
        when(userRepository.save(any(User.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0)
        );
        boolean success=userService.register(registerRequest);

        //then
        assertTrue(success);

    }
    @Test
    void register_NoRolesEntered() {
        //given
        RegisterRequest registerRequest=new RegisterRequest("Ahmet","test", Collections.emptySet());

        //then
        assertThrows(NoRolesException.class,
                ()->userService.register(registerRequest));

    }
    @Test
    void register_NoValidRolesEntered() {
        //given
        RegisterRequest registerRequest=new RegisterRequest("Ahmet","test", Set.of(""));

        //when
        when(passwordEncoder.encode(anyString())).thenReturn("...");
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        //then
        assertThrows(NoRolesException.class,
                ()->userService.register(registerRequest)
        );

    }

    @Test
    void login_Success() {
        //given
        AuthenticationRequest authenticationRequest=new AuthenticationRequest("tim","test");
        User user=new User(1L,"tim","test",false,Set.of(new Role()));

        //when
        when(userRepository.findByUsernameAndDeletedIsFalse(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        AuthenticationResponse response=userService.login(authenticationRequest);

        //then
        assertNotNull(response.getToken());
    }

    @Test
    void login_Fail() {
        //given
        AuthenticationRequest authenticationRequest=new AuthenticationRequest("tim","test");
        AuthenticationException authenticationException=new AuthenticationException("Failed Authentication"){};
        //when
        when(authenticationManager.authenticate(any())).thenThrow(authenticationException);


        //then
        assertThrows(InvalidAuthenticationException.class,
                ()->userService.login(authenticationRequest));
    }

    @Test
    void verify_success() {
        //given
        HttpServletRequest request=mock(HttpServletRequest.class);
        String permission1="UserManagement";
        String permission2="ErrorLogin";
        User user=new User(1L,"username","password",false,null);
        Set<Permission> permissions=Set.of(new Permission(1L,permission1,"")
        ,new Permission(2L,permission2,""));
        Set<Role> roles=Set.of(new Role(1L,"Admin","", List.of(user),permissions),
                new Role(1L,"Operator","", List.of(user),permissions));
        user.setRoles(roles);
        //when
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");
        when(jwtService.extractUsername(anyString())).thenReturn("username");
        when(userRepository.findByUsernameAndDeletedIsFalse(anyString())).thenReturn(Optional.of(user));
        Set<String> result=userService.verify(request);

        //then
        assertTrue(result.contains(permission1));
        assertTrue(result.contains(permission2));

    }

    @Test
    void verify_fail() {
        //given
        HttpServletRequest request=mock(HttpServletRequest.class);
        //when
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");
        when(jwtService.extractUsername(anyString())).thenReturn("username");
        when(userRepository.findByUsernameAndDeletedIsFalse(anyString())).thenReturn(Optional.empty());
        Set<String> result=userService.verify(request);

        //then
        assertNull(result);
    }

    @Test
    void delete_Success() {
        //given
        String username="username";
        User user=new User(1L,"username","password",false,null);
        //when
        when(userRepository.findByUsernameAndDeletedIsFalse(username)).thenReturn(Optional.of(user));

        Boolean result=userService.delete(username);

        //then
        assertTrue(user.isDeleted());
        assertTrue(result);
    }
    @Test
    void delete_UserNotFound() {
        //given
        String username="username";
        //when
        when(userRepository.findByUsernameAndDeletedIsFalse(username)).thenReturn(Optional.empty());

        //then
        assertThrows(UserNotFoundException.class,
                ()->userService.delete(username));
    }

    @Test
    void updateUsername_Success() {
        //given
        User user=new User(1L,"username","password",false,null);
        String username="username";
        String newUsername="updatedUser";
        //when
        when(userRepository.findByUsernameAndDeletedIsFalse(username)).thenReturn(Optional.of(user));

        Boolean success=userService.updateUsername(newUsername,username);

        //then
        assertTrue(success);
        assertEquals(newUsername,user.getUsername());
    }
    @Test
    void updateUsername_UserNotFound() {
        //given
        String username="username";
        String newUsername="updatedUser";
        //when
        when(userRepository.findByUsernameAndDeletedIsFalse(username)).thenReturn(Optional.empty());

        //then
        assertThrows(UserNotFoundException.class,()->userService.updateUsername(newUsername,username));
    }

    @Test
    void updateUsername_UsernameTaken() {
        //given
        String username="username";
        String newUsername="updatedUser";
        //when
        when(userRepository.existsByUsernameAndDeletedIsFalse(anyString())).thenReturn(true);

        //then
        assertThrows(UsernameTakenException.class,()->userService.updateUsername(newUsername,username));
    }



    @Test
    void changePassword() {
        //given
        HttpServletRequest request=mock(HttpServletRequest.class);
        User user=new User(1L,"username","password",false,null);
        String username="username";
        PasswordsDTO passwordsDTO=new PasswordsDTO("password","newPassword");
        //when
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");
        when(jwtService.extractUsername(anyString())).thenReturn("username");
        when(userRepository.findByUsernameAndDeletedIsFalse(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("newPassword");

        boolean success=userService.changePassword(request,passwordsDTO);

        //then
        assertTrue(success);
        assertEquals(passwordsDTO.getNewPassword(),user.getPassword());
    }
    @Test
    void changePassword_FalsePassword() {
        //given
        HttpServletRequest request=mock(HttpServletRequest.class);
        User user=new User(1L,"username","password",false,null);
        String username="username";
        PasswordsDTO passwordsDTO=new PasswordsDTO("passwords","newPassword");
        //when
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");
        when(jwtService.extractUsername(anyString())).thenReturn("username");
        when(userRepository.findByUsernameAndDeletedIsFalse(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(false);

        //then
        assertThrows(IncorrectPasswordException.class,
                ()->userService.changePassword(request,passwordsDTO) );
    }
    @Test
    void changePassword_UserNotFound() {
        //given
        HttpServletRequest request=mock(HttpServletRequest.class);
        String username="username";
        PasswordsDTO passwordsDTO=new PasswordsDTO("passwords","newPassword");
        //when
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");
        when(jwtService.extractUsername(anyString())).thenReturn("username");
        when(userRepository.findByUsernameAndDeletedIsFalse(username)).thenReturn(Optional.empty());

        //then
        assertThrows(UserNotFoundException.class,
                ()->userService.changePassword(request,passwordsDTO) );
    }
    @Test
    void changePassword_BearerFail() {
        //given
        HttpServletRequest request=mock(HttpServletRequest.class);
        PasswordsDTO passwordsDTO=new PasswordsDTO("passwords","newPassword");
        //when
        when(request.getHeader("Authorization")).thenReturn("");

        boolean success=userService.changePassword(request,passwordsDTO);
        //then
        assertFalse(success);
    }
    @Test
    void verifyAndUsername() {
        //given
        HttpServletRequest request=mock(HttpServletRequest.class);
        String permission1="UserManagement";
        String permission2="ErrorLogin";
        User user=new User(1L,"username","password",false,null);
        Set<Permission> permissions=Set.of(new Permission(1L,permission1,"")
                ,new Permission(2L,permission2,""));
        Set<Role> roles=Set.of(new Role(1L,"Admin","", List.of(user),permissions),
                new Role(1L,"Operator","", List.of(user),permissions));
        user.setRoles(roles);
        //when
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");
        when(jwtService.extractUsername(anyString())).thenReturn("username");
        when(userRepository.findByUsernameAndDeletedIsFalse(anyString())).thenReturn(Optional.of(user));
        Map<String,String> result=userService.verifyAndUsername(request);

        //then
        assertTrue(result.containsKey(permission1));
        assertTrue(result.containsKey(permission2));
        assertTrue(result.containsKey("Username"));
        assertEquals(user.getUsername(),result.get("Username"));
    }
    @Test
    void verifyAndUsername_UserNotFound() {
        //given
        HttpServletRequest request=mock(HttpServletRequest.class);
        //when
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");
        when(jwtService.extractUsername(anyString())).thenReturn("username");
        when(userRepository.findByUsernameAndDeletedIsFalse(anyString())).thenReturn(Optional.empty());
        Map<String,String> result=userService.verifyAndUsername(request);

        //then
        assertNull(result);
    }

    @Test
    void addRole_Success() {
        //given
        Set<Role> roles=new HashSet<>();
        roles.add(new Role());
        User user=new User(1L,"username","password",false,roles);
        Role role=new Role(1L,"Admin","",null,null);
        String username="username";
        String roleStr="Admin";
        //when
        when(userRepository.findByUsernameAndDeletedIsFalse(anyString())).thenReturn(Optional.of(user));
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));

        boolean success=userService.addRole(username,roleStr);

        //then
        assertTrue(success);
        assertEquals(2,user.getRoles().size());
    }
    @Test
    void addRole_UserNotFound() {
        //given
        String username="username";
        String roleStr="Admin";

        //then
        assertThrows(UserNotFoundException.class,()->userService.addRole(username,roleStr));
    }
    @Test
    void addRole_RoleNotFound() {
        //given
        Set<Role> roles=new HashSet<>();
        roles.add(new Role());
        User user=new User(1L,"username","password",false,roles);
        String username="username";
        String roleStr="Admin";
        //when
        when(userRepository.findByUsernameAndDeletedIsFalse(anyString())).thenReturn(Optional.of(user));
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        //then
        assertThrows(RoleNotFoundException.class,()->userService.addRole(username,roleStr));
        assertEquals(1,user.getRoles().size());
    }

    @Test
    void removeRole_Success() {
        //given
        Set<Role> roles=new HashSet<>();
        Role role=new Role(1L,"Admin","",null,null);
        roles.add(new Role());
        roles.add(role);
        User user=new User(1L,"username","password",false,roles);
        String username="username";
        String roleStr="Admin";
        //when
        when(userRepository.findByUsernameAndDeletedIsFalse(anyString())).thenReturn(Optional.of(user));
        when(roleRepository.findByName(anyString())).thenReturn(Optional.of(role));

        boolean success=userService.removeRole(username,roleStr);

        //then
        assertTrue(success);
        assertEquals(1,user.getRoles().size());
        assertFalse(user.getRoles().contains(role));
    }
    @Test
    void removeRole_UserNotFound() {
        //given
        String username="username";
        String roleStr="Admin";
        //when
        when(userRepository.findByUsernameAndDeletedIsFalse(anyString())).thenReturn(Optional.empty());

        //then
        assertThrows(UserNotFoundException.class,()->userService.removeRole(username,roleStr));
    }
    @Test
    void removeRole_RoleNotFound() {
        //given
        Set<Role> roles=new HashSet<>();
        roles.add(new Role());
        User user=new User(1L,"username","password",false,roles);
        String username="username";
        String roleStr="Admin";

        //when
        when(userRepository.findByUsernameAndDeletedIsFalse(anyString())).thenReturn(Optional.of(user));
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        //then
        assertThrows(RoleNotFoundException.class,()->userService.removeRole(username,roleStr));
        assertEquals(1,user.getRoles().size());
    }
}