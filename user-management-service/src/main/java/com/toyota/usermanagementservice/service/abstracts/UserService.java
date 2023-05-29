package com.toyota.usermanagementservice.service.abstracts;

import com.toyota.usermanagementservice.dto.UserDTO;
import com.toyota.usermanagementservice.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    boolean create(UserDTO userDTO);
    UserResponse update(HttpServletRequest request,Long userId,UserDTO userDTO);
    UserResponse deleteUser(HttpServletRequest request,Long userId);

//    List<UserResponse> getAll(int page,int size,String attribute,String desired,String direction,String sortField);
}
