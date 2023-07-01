package com.toyota.usermanagementservice.service.impl;

import com.toyota.usermanagementservice.dao.UserRepository;
import com.toyota.usermanagementservice.domain.Gender;
import com.toyota.usermanagementservice.domain.Role;
import com.toyota.usermanagementservice.domain.User;
import com.toyota.usermanagementservice.dto.UserDTO;
import com.toyota.usermanagementservice.dto.UserResponse;
import com.toyota.usermanagementservice.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient.ResponseSpec responseSpec;
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    private UserServiceImpl userService;
    @BeforeEach
    void setUp() {
        userService=new UserServiceImpl(userRepository,webClientBuilder,new ModelMapper());
    }

    @Test
    void create_Success() {
        //given
        UserDTO userDTO=new UserDTO("firstname","lastname","username",
                "email@gmail.com","abcdef1",Set.of(Role.OPERATOR), Gender.FEMALE);

        //when
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(Mockito.anyString()))
                .thenReturn(requestBodyUriSpec);
        doReturn(requestHeadersSpec).when(requestBodyUriSpec).bodyValue(any());
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(),any())).thenReturn(responseSpec);
        Mono<Boolean> mono = Mono.just(true);
        Mono<Boolean> monoSpy=Mockito.spy(mono);
        when(responseSpec.bodyToMono(Boolean.class))
                .thenReturn(monoSpy);
        when(userRepository.existsByUsernameAndDeletedIsFalse(anyString())).thenReturn(false);
        when(userRepository.existsByEmailAndDeletedIsFalse(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        UserResponse response=userService.create(userDTO);

        //then
        Mockito.verify(userRepository).save(any(User.class));
        assertEquals(userDTO.getFirstname(),response.getFirstname());
        assertEquals(userDTO.getLastname(),response.getLastname());
        assertEquals(userDTO.getEmail(),response.getEmail());
        assertEquals(userDTO.getUsername(),response.getUsername());
        assertEquals(userDTO.getRole(),response.getRole());
        assertEquals(userDTO.getGender(),response.getGender());
    }
    @Test
    void create_Fail() {
        //given
        UserDTO userDTO=new UserDTO("firstname","lastname","username",
                "email@gmail.com","abcdef1",Set.of(Role.OPERATOR), Gender.FEMALE);

        //when
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(Mockito.anyString()))
                .thenReturn(requestBodyUriSpec);
        doReturn(requestHeadersSpec).when(requestBodyUriSpec).bodyValue(any());
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(),any())).thenReturn(responseSpec);
        Mono<Boolean> mono = Mono.just(false);
        Mono<Boolean> monoSpy=Mockito.spy(mono);
        when(responseSpec.bodyToMono(Boolean.class))
                .thenReturn(monoSpy);
        when(userRepository.existsByUsernameAndDeletedIsFalse(anyString())).thenReturn(false);
        when(userRepository.existsByEmailAndDeletedIsFalse(anyString())).thenReturn(false);

        //then
        assertThrows(UnexpectedException.class,
                ()->userService.create(userDTO));
    }
    @Test
    void create_UsernameAlreadyExists() {
        //given
        UserDTO userDTO=new UserDTO("firstname","lastname","username",
                "email@gmail.com","abcdef1",Set.of(Role.OPERATOR), Gender.FEMALE);

        //when
        when(userRepository.existsByUsernameAndDeletedIsFalse(anyString())).thenReturn(true);

        //then
        assertThrows(UserAlreadyExistsException.class,
                ()->userService.create(userDTO));
    }
    @Test
    void create_EmailAlreadyExists() {
        //given
        UserDTO userDTO=new UserDTO("firstname","lastname","username",
                "email@gmail.com","abcdef1",Set.of(Role.OPERATOR), Gender.FEMALE);

        //when
        when(userRepository.existsByEmailAndDeletedIsFalse(anyString())).thenReturn(true);

        //then
        assertThrows(UserAlreadyExistsException.class,
                ()->userService.create(userDTO));
    }

    @Test
    void update_Success() {
        //given
        UserDTO userDTO=new UserDTO("firstname","lastname","username",
                "email@gmail.com","abcdef1",Set.of(Role.OPERATOR), Gender.FEMALE);
        User existingUser=new User(1L,"test","test","test",
                "test@gmail.com",Set.of(Role.OPERATOR), Gender.MALE,false);
        //when

        //WebClient mock
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString(),(Object) any())).thenReturn(requestBodySpec);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("Token");
        when(requestBodySpec.headers(argThat(consumer -> {
            consumer.accept(headers);
            return true;
        }))).thenReturn(requestBodyUriSpec);
        doReturn(requestHeadersSpec).when(requestBodyUriSpec).bodyValue(anyString());
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(),any())).thenReturn(responseSpec);
        Mono<Boolean> mono = Mono.just(true);
        Mono<Boolean> monoSpy=Mockito.spy(mono);
        when(responseSpec.bodyToMono(Boolean.class))
                .thenReturn(monoSpy);
        //repository mock
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(userRepository.findById(any())).thenReturn(Optional.of(existingUser));
        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");

        UserResponse response=userService.update(request,1L,userDTO);

        //then
        Mockito.verify(userRepository).save(any(User.class));
        assertEquals(userDTO.getFirstname(),response.getFirstname());
        assertEquals(userDTO.getLastname(),response.getLastname());
        assertEquals(userDTO.getEmail(),response.getEmail());
        assertEquals(userDTO.getUsername(),response.getUsername());
        assertEquals(userDTO.getRole(),response.getRole());
        assertEquals(userDTO.getGender(),response.getGender());
    }
    @Test
    void update_UnexpectedFail() {
        //given
        UserDTO userDTO=new UserDTO("firstname","lastname","username",
                "email@gmail.com","abcdef1",Set.of(Role.OPERATOR), Gender.FEMALE);
        User existingUser=new User(1L,"test","test","test",
                "test@gmail.com",Set.of(Role.OPERATOR), Gender.MALE,false);
        //when
        //HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("Token");
        //WebClient mock
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString(),(Object) any())).thenReturn(requestBodySpec);
        when(requestBodySpec.headers(argThat(consumer -> {
            consumer.accept(headers);
            return true;
        }))).thenReturn(requestBodyUriSpec);
        doReturn(requestHeadersSpec).when(requestBodyUriSpec).bodyValue(anyString());
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(),any())).thenReturn(responseSpec);
        Mono<Boolean> mono = Mono.just(false);
        Mono<Boolean> monoSpy=Mockito.spy(mono);
        when(responseSpec.bodyToMono(Boolean.class))
                .thenReturn(monoSpy);
        //repository mock
        when(userRepository.findById(any())).thenReturn(Optional.of(existingUser));
        //request mock
        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");

        //then
        assertThrows(UnexpectedException.class,()->userService.update(request,1L,userDTO));
    }
    @Test
    void update_UserNotFound() {
        //given
        UserDTO userDTO=new UserDTO();
        //when
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);

        //then
        assertThrows(UserNotFoundException.class,()->userService.update(request,1L,userDTO));
    }
    @Test
    void update_UsernameTaken() {
        //given
        UserDTO userDTO=new UserDTO();
        userDTO.setUsername("Username");
        User user=new User();
        user.setUsername("oldUsername");
        //when
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        when(userRepository.existsByUsernameAndDeletedIsFalse(anyString())).thenReturn(true);

        //then
        assertThrows(UserAlreadyExistsException.class,()->userService.update(request,1L,userDTO));
    }
    @Test
    void update_EmailTaken() {
        //given
        UserDTO userDTO=new UserDTO();
        userDTO.setEmail("email");
        User user=new User();
        user.setEmail("oldEmail");
        //when
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        when(userRepository.existsByEmailAndDeletedIsFalse(anyString())).thenReturn(true);

        //then
        assertThrows(UserAlreadyExistsException.class,()->userService.update(request,1L,userDTO));
    }

    @Test
    void deleteUser_Success() {
        //given
        User existingUser=new User(1L,"test","test","test",
                "test@gmail.com",Set.of(Role.OPERATOR), Gender.MALE,false);

        //when
        //WebClient mock
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("Token");
        when(requestBodySpec.headers(argThat(consumer -> {
            consumer.accept(headers);
            return true;
        }))).thenReturn(requestBodyUriSpec);
        doReturn(requestHeadersSpec).when(requestBodyUriSpec).bodyValue(anyString());
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(),any())).thenReturn(responseSpec);
        Mono<Boolean> mono = Mono.just(true);
        Mono<Boolean> monoSpy=Mockito.spy(mono);
        when(responseSpec.bodyToMono(Boolean.class))
                .thenReturn(monoSpy);
        //repository mock
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(userRepository.findById(any())).thenReturn(Optional.of(existingUser));
        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");

        UserResponse response=userService.deleteUser(request,1L);

        //then
        Mockito.verify(userRepository).save(any(User.class));
        assertTrue(existingUser.isDeleted());
        assertEquals(existingUser.getUsername(),response.getUsername());
    }

    @Test
    void deleteUser_UserNotFound() {
        //given
        Long userId=1L;
        //when
        //repository mock
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");

        //then
        assertThrows(UserNotFoundException.class,
                ()->userService.deleteUser(request,userId));
    }

    @Test
    void deleteUser_UnexpectedFail() {
        //given
        User existingUser=new User(1L,"test","test","test",
                "test@gmail.com",Set.of(Role.OPERATOR), Gender.MALE,false);
        Long userId=1L;
        //when
        //WebClient mock
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("Token");
        when(requestBodySpec.headers(argThat(consumer -> {
            consumer.accept(headers);
            return true;
        }))).thenReturn(requestBodyUriSpec);
        doReturn(requestHeadersSpec).when(requestBodyUriSpec).bodyValue(anyString());
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(),any())).thenReturn(responseSpec);
        Mono<Boolean> mono = Mono.just(false);
        Mono<Boolean> monoSpy=Mockito.spy(mono);
        when(responseSpec.bodyToMono(Boolean.class))
                .thenReturn(monoSpy);
        //repository mock
        when(userRepository.findById(any())).thenReturn(Optional.of(existingUser));
        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");


        //then
        assertThrows(UnexpectedException.class,()->userService.deleteUser(request,userId));
    }


    @Test
    void getAll_Asc() {
        //given
        String firstname="firstname";
        String lastname="lastname";
        String username="username";
        String email="email";
        int page=1;
        int size=3;
        List<String> sortList=List.of("firstname");
        String sortOrder="Asc";
        Sort.Order sort=new Sort.Order(Sort.Direction.ASC,sortList.get(0));
        Pageable pageable= PageRequest.of(page,size, Sort.by(sort));
        List<User> content=List.of(new User());
        //when
        Page<User> pageMock=new PageImpl<>(content,pageable,1);
        when(userRepository.getUsersFiltered(firstname,lastname,email,username,pageable))
                .thenReturn(pageMock);
        Page<UserResponse> response=userService.getAll(firstname,lastname,username,email,
                page,size,sortList,sortOrder);

        //then
        assertEquals(UserResponse.class,response.getContent().get(0).getClass());
        assertEquals(page,response.getPageable().getPageNumber());
        assertEquals(size,response.getPageable().getPageSize());


    }
    @Test
    void getAll_Desc() {
        //given
        String firstname="firstname";
        String lastname="lastname";
        String username="username";
        String email="email";
        int page=1;
        int size=3;
        List<String> sortList=List.of("firstname");
        String sortOrder="Desc";
        Sort.Order sort=new Sort.Order(Sort.Direction.DESC,sortList.get(0));
        Pageable pageable= PageRequest.of(page,size, Sort.by(sort));
        List<User> content=List.of(new User());
        //when
        Page<User> pageMock=new PageImpl<>(content,pageable,1);
        when(userRepository.getUsersFiltered(firstname,lastname,email,username,pageable))
                .thenReturn(pageMock);
        Page<UserResponse> response=userService.getAll(firstname,lastname,username,email,
                page,size,sortList,sortOrder);

        //then
        assertEquals(UserResponse.class,response.getContent().get(0).getClass());
        assertEquals(page,response.getPageable().getPageNumber());
        assertEquals(size,response.getPageable().getPageSize());


    }

    @Test
    void addRole() {
        //given
        Set<Role> roles=new HashSet<>();
        roles.add(Role.OPERATOR);
        User existingUser=new User(1L,"test","test","test",
                "test@gmail.com",roles, Gender.MALE,false);
        Long userId=1L;

        //when
        //WebClient mock
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString(),(Object) any())).thenReturn(requestBodySpec);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("Token");
        when(requestBodySpec.headers(argThat(consumer -> {
            consumer.accept(headers);
            return true;
        }))).thenReturn(requestBodyUriSpec);
        doReturn(requestHeadersSpec).when(requestBodyUriSpec).bodyValue(anyString());
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(),any())).thenReturn(responseSpec);
        Mono<Boolean> mono = Mono.just(true);
        Mono<Boolean> monoSpy=Mockito.spy(mono);
        when(responseSpec.bodyToMono(Boolean.class))
                .thenReturn(monoSpy);
        //repository mock
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(userRepository.findById(any())).thenReturn(Optional.of(existingUser));

        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");

        UserResponse response=userService.addRole(request,userId,Role.ADMIN);

        //then
        Mockito.verify(userRepository).save(any(User.class));
        assertEquals(2,response.getRole().size());
        assertTrue(response.getRole().contains(Role.ADMIN));
    }
    @Test
    void addRole_UnexpectedFail() {
        //given
        Set<Role> roles=new HashSet<>();
        roles.add(Role.OPERATOR);
        User existingUser=new User(1L,"test","test","test",
                "test@gmail.com",roles, Gender.MALE,false);
        Long userId=1L;

        //when
        //WebClient mock
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString(),(Object) any())).thenReturn(requestBodySpec);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("Token");
        when(requestBodySpec.headers(argThat(consumer -> {
            consumer.accept(headers);
            return true;
        }))).thenReturn(requestBodyUriSpec);
        doReturn(requestHeadersSpec).when(requestBodyUriSpec).bodyValue(anyString());
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(),any())).thenReturn(responseSpec);
        Mono<Boolean> mono = Mono.just(false);
        Mono<Boolean> monoSpy=Mockito.spy(mono);
        when(responseSpec.bodyToMono(Boolean.class))
                .thenReturn(monoSpy);

        //repository mock
        when(userRepository.findById(any())).thenReturn(Optional.of(existingUser));

        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");


        //then
        assertThrows(UnexpectedException.class,
                ()->userService.addRole(request,userId,Role.ADMIN));
    }
    @Test
    void addRole_AlreadyExists() {
        //given
        Set<Role> roles=new HashSet<>();
        roles.add(Role.OPERATOR);
        User existingUser=new User(1L,"test","test","test",
                "test@gmail.com",roles, Gender.MALE,false);
        Long userId=1L;

        //when
        //repository mock
        when(userRepository.findById(any())).thenReturn(Optional.of(existingUser));

        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");
        //then
        assertThrows(RoleAlreadyExistsException.class,
                ()->userService.addRole(request,userId,Role.OPERATOR));
    }
    @Test
    void addRole_UserNotFound() {
        //given
        Long userId=1L;

        //when
        //repository mock
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        //then
        assertThrows(UserNotFoundException.class,
                ()->userService.addRole(request,userId,Role.OPERATOR));
    }

    @Test
    void removeRole() {
        //given
        Set<Role> roles=new HashSet<>();
        roles.add(Role.OPERATOR);
        roles.add(Role.LEADER);
        User existingUser=new User(1L,"test","test","test",
                "test@gmail.com",roles, Gender.MALE,false);
        Long userId=1L;

        //when
        //WebClient mock
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString(),(Object) any())).thenReturn(requestBodySpec);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("Token");
        when(requestBodySpec.headers(argThat(consumer -> {
            consumer.accept(headers);
            return true;
        }))).thenReturn(requestBodyUriSpec);
        doReturn(requestHeadersSpec).when(requestBodyUriSpec).bodyValue(anyString());
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(),any())).thenReturn(responseSpec);
        Mono<Boolean> mono = Mono.just(true);
        Mono<Boolean> monoSpy=Mockito.spy(mono);
        when(responseSpec.bodyToMono(Boolean.class))
                .thenReturn(monoSpy);
        //repository mock
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(userRepository.findById(any())).thenReturn(Optional.of(existingUser));

        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");

        UserResponse response=userService.removeRole(request,userId,Role.OPERATOR);

        //then
        Mockito.verify(userRepository).save(any(User.class));
        assertEquals(1,response.getRole().size());
        assertFalse(response.getRole().contains(Role.ADMIN));
    }
    @Test
    void removeRole_UnexpectedFail() {
        //given
        Set<Role> roles=new HashSet<>();
        roles.add(Role.OPERATOR);
        roles.add(Role.LEADER);
        User existingUser=new User(1L,"test","test","test",
                "test@gmail.com",roles, Gender.MALE,false);
        Long userId=1L;

        //when
        //WebClient mock
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString(),(Object) any())).thenReturn(requestBodySpec);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("Token");
        when(requestBodySpec.headers(argThat(consumer -> {
            consumer.accept(headers);
            return true;
        }))).thenReturn(requestBodyUriSpec);
        doReturn(requestHeadersSpec).when(requestBodyUriSpec).bodyValue(anyString());
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(),any())).thenReturn(responseSpec);
        Mono<Boolean> mono = Mono.just(false);
        Mono<Boolean> monoSpy=Mockito.spy(mono);
        when(responseSpec.bodyToMono(Boolean.class))
                .thenReturn(monoSpy);
        //repository mock

        when(userRepository.findById(any())).thenReturn(Optional.of(existingUser));

        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");

        //then
        assertThrows(UnexpectedException.class,
                ()->userService.removeRole(request,userId,Role.OPERATOR));
    }
    @Test
    void removeRole_SingleRemovalFail() {
        //given
        Set<Role> roles=new HashSet<>();
        roles.add(Role.OPERATOR);
        User existingUser=new User(1L,"test","test","test",
                "test@gmail.com",roles, Gender.MALE,false);
        Long userId=1L;

        //when
        when(userRepository.findById(any())).thenReturn(Optional.of(existingUser));

        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");

        //then
        assertThrows(SingleRoleRemovalException.class,
                ()->userService.removeRole(request,userId,Role.OPERATOR));
    }
    @Test
    void removeRole_RoleNotFound() {
        //given
        Set<Role> roles=new HashSet<>();
        roles.add(Role.OPERATOR);
        roles.add(Role.LEADER);
        User existingUser=new User(1L,"test","test","test",
                "test@gmail.com",roles, Gender.MALE,false);
        Long userId=1L;

        //when
        when(userRepository.findById(any())).thenReturn(Optional.of(existingUser));

        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer Token");

        //then
        assertThrows(RoleNotFoundException.class,
                ()->userService.removeRole(request,userId,Role.ADMIN));
    }
    @Test
    void removeRole_UserNotFound() {
        //given
        Long userId=1L;

        //when
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);

        //then
        assertThrows(UserNotFoundException.class,
                ()->userService.removeRole(request,userId,Role.ADMIN));
    }
}