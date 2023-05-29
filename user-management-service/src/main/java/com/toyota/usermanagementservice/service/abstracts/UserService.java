package com.toyota.usermanagementservice.service.abstracts;

import com.toyota.usermanagementservice.dto.UserDTO;
import com.toyota.usermanagementservice.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    boolean create(UserDTO userDTO);
    UserResponse update(HttpServletRequest request,Long userId,UserDTO userDTO);
    UserResponse deleteUser(HttpServletRequest request,Long userId);

    Page<UserResponse> getAll(String filterFirstname, String filterLastname, String filterUsername, String filterEmail,
                              int page, int size, List<String> sortList, String sortOrder);
}
