package com.toyota.usermanagementservice.resource;

import com.toyota.usermanagementservice.domain.Gender;
import com.toyota.usermanagementservice.domain.Role;
import com.toyota.usermanagementservice.dto.UserDTO;
import com.toyota.usermanagementservice.dto.UserResponse;
import com.toyota.usermanagementservice.service.abstracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    @Test
    void getAll() {
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
        List<UserResponse> content=List.of(new UserResponse());

        //when
        Page<UserResponse> pageMock=new PageImpl<>(content,pageable,1);
        Mockito.when(userService.getAll(firstname,lastname,username
                , email,page,size,sortList,sortOrder)).thenReturn(pageMock);
        Page<UserResponse> pageResponse=userController.getAll(firstname,lastname,username
                , email,page,size,sortList,sortOrder);
        //then
        Mockito.verify(userService).getAll(firstname,lastname,username
                , email,page,size,sortList,sortOrder);
        assertNotNull(pageResponse);
        assertEquals(content,pageResponse.getContent());


    }

    @Test
    void create() {
        //given
        UserDTO userDTO=new UserDTO("firstname","lastname","username",
                "email@gmail.com","abcdef1", Set.of(Role.OPERATOR), Gender.FEMALE);
        UserResponse userResponse=new UserResponse(1L,"firstname","lastname","username",
                "email@gmail.com", Set.of(Role.OPERATOR), Gender.FEMALE);
        //when
        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        Mockito.when(userService.create(any(), any(UserDTO.class))).thenReturn(userResponse);
        ResponseEntity<UserResponse> result=userController.create(request,userDTO);

        //then
        Mockito.verify(userService).create(any(), any(UserDTO.class));
        assertEquals(HttpStatus.CREATED,result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(userDTO.getUsername(),result.getBody().getUsername());
    }

    @Test
    void update() {
        //given
        UserDTO userDTO=new UserDTO();
        UserResponse userResponse=new UserResponse(1L,"updated","updated","updated",
                "updated@gmail.com", Set.of(Role.OPERATOR), Gender.FEMALE);
        //when
        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        Mockito.when(userService.update(any(),Mockito.anyLong(), any(UserDTO.class)))
                .thenReturn(userResponse);
        ResponseEntity<UserResponse> result=userController.update(request,1L,userDTO);

        //then
        Mockito.verify(userService).update(any(),Mockito.anyLong(), any(UserDTO.class));
        assertEquals(HttpStatus.OK,result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(userResponse.getUsername(),result.getBody().getUsername());
    }

    @Test
    void addRole() {
        //given
        UserResponse userResponse=new UserResponse();
        //when
        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        Mockito.when(userService.addRole(any(),Mockito.anyLong(), any()))
                .thenReturn(userResponse);
        ResponseEntity<UserResponse> result=userController.addRole(request,1L,Role.ADMIN);

        //then
        Mockito.verify(userService).addRole(any(),Mockito.anyLong(), any());
        assertEquals(HttpStatus.OK,result.getStatusCode());
    }

    @Test
    void removeRole() {
        //given
        UserResponse userResponse=new UserResponse();
        //when
        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        Mockito.when(userService.removeRole(any(),Mockito.anyLong(), any()))
                .thenReturn(userResponse);
        ResponseEntity<UserResponse> result=userController.removeRole(request,1L,Role.ADMIN);

        //then
        Mockito.verify(userService).removeRole(any(),Mockito.anyLong(), any());
        assertEquals(HttpStatus.OK,result.getStatusCode());
    }

    @Test
    void deleteUser() {
        //given
        UserResponse userResponse=new UserResponse();
        //when
        HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
        Mockito.when(userService.deleteUser(any(),Mockito.anyLong()))
                .thenReturn(userResponse);
        ResponseEntity<UserResponse> result=userController.deleteUser(request,1L);

        //then
        Mockito.verify(userService).deleteUser(any(),Mockito.anyLong());
        assertNotNull(result);
        assertEquals(HttpStatus.OK,result.getStatusCode());
    }
}