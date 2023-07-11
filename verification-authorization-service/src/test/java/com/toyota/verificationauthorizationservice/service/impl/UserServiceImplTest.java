package com.toyota.verificationauthorizationservice.service.impl;

import com.toyota.verificationauthorizationservice.dao.RoleRepository;
import com.toyota.verificationauthorizationservice.dao.TokenRepository;
import com.toyota.verificationauthorizationservice.dao.UserRepository;
import com.toyota.verificationauthorizationservice.domain.Role;
import com.toyota.verificationauthorizationservice.domain.Token;
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
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Mock
    private TokenRepository tokenRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @Test
    void register_success() {
        //given
        RegisterRequest registerRequest=new RegisterRequest("Ahmet","test",Set.of(""));
        Role existingRole=new Role();

        //when
        when(userRepository.existsByUsernameAndDeletedIsFalse(anyString())).thenReturn(false);
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
    void register_UserAlreadyExists() {
        //given
        RegisterRequest registerRequest=new RegisterRequest("Ahmet","test",Set.of(""));

        //when
        when(userRepository.existsByUsernameAndDeletedIsFalse(anyString())).thenReturn(true);

        //then
        assertThrows(UsernameTakenException.class,()->userService.register(registerRequest));

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
        User user=new User(1L,"tim","test",false,Set.of(new Role()),List.of(new Token()));

        //when
        when(userRepository.findByUsernameAndDeletedIsFalse(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("token");
        when(tokenRepository.findAllValidTokensByUser(any())).thenReturn(List.of(new Token()));
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
    void delete_Success() {
        //given
        String username="username";
        User user=new User(1L,"username","password",false,null,List.of(new Token()));
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
        User user=new User(1L,"username","password",false,null,List.of(new Token()));
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
        User user=new User(1L,"username","password",false,null,List.of(new Token()));
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
        User user=new User(1L,"username","password",false,null,List.of(new Token()));
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
    void verify() {
        //given
        String username="username";
        MockedStatic<SecurityContextHolder> securityContextHolder=mockStatic(SecurityContextHolder.class);
        SecurityContext securityContext=mock(SecurityContext.class);
        Authentication authentication=mock(Authentication.class);
        //when
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        when(authentication.getAuthorities()).thenReturn(new ArrayList<>());
        Map<String,String> result=userService.verify();

        //then
        assertTrue(result.containsKey("Username"));
        assertEquals(username,result.get("Username"));
        assertEquals(1,result.size());
        securityContextHolder.close();
    }

    @Test
    void addRole_Success() {
        //given
        Set<Role> roles=new HashSet<>();
        roles.add(new Role());
        User user=new User(1L,"username","password",false,roles,List.of(new Token()));
        Role role=new Role(1L,"Admin","",null);
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
        User user=new User(1L,"username","password",false,roles,List.of(new Token()));
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
        Role role=new Role(1L,"Admin","",null);
        roles.add(new Role());
        roles.add(role);
        User user=new User(1L,"username","password",false,roles,List.of(new Token()));
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
        User user=new User(1L,"username","password",false,roles,List.of(new Token()));
        String username="username";
        String roleStr="Admin";

        //when
        when(userRepository.findByUsernameAndDeletedIsFalse(anyString())).thenReturn(Optional.of(user));
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        //then
        assertThrows(RoleNotFoundException.class,()->userService.removeRole(username,roleStr));
        assertEquals(1,user.getRoles().size());
    }

    @Test
    void logout() {
        //given
        String jwtToken="Bearer token";
        User user=new User();
        Date currentDate=new Date();
        Token token=new Token("id",false,new Date(currentDate.getTime()+60000),user);
        //when
        when(jwtService.extractTokenId(anyString())).thenReturn("jti");
        when(tokenRepository.findById(anyString())).thenReturn(Optional.of(token));

        userService.logout(jwtToken);

        //then
        assertTrue(token.getExpirationDate().after(new Date()));
        assertTrue(token.isRevoked());
    }
    @Test
    void logout_NoUserForProvidedToken() {
        //given
        String jwtToken="Bearer token";
        //when
        when(jwtService.extractTokenId(anyString())).thenReturn("jti");
        when(tokenRepository.findById(anyString())).thenReturn(Optional.empty());

        //then
        assertThrows(UserNotFoundException.class,()->userService.logout(jwtToken));
    }
    @Test
    void logout_InvalidBearer() {
        //given
        String jwtToken="";
        //when

        //then
        assertThrows(InvalidBearerToken.class,()->userService.logout(jwtToken));
    }
}