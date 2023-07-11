package com.toyota.usermanagementservice.service.abstracts;

import com.toyota.usermanagementservice.domain.Role;
import com.toyota.usermanagementservice.dto.UserDTO;
import com.toyota.usermanagementservice.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    UserResponse create(HttpServletRequest request,UserDTO userDTO);
    UserResponse update(HttpServletRequest request,Long userId,UserDTO userDTO);
    UserResponse deleteUser(HttpServletRequest request,Long userId);

    Page<UserResponse> getAll(String firstname, String lastname, String username, String email,
                              int page, int size, List<String> sortList, String sortOrder);

    UserResponse addRole(HttpServletRequest request,Long userId,Role roles);

    UserResponse removeRole(HttpServletRequest request, Long userId, Role role);
}
