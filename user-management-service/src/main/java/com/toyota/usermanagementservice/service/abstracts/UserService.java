package com.toyota.usermanagementservice.service.abstracts;

import com.toyota.usermanagementservice.dto.UserDTO;

public interface UserService {
    boolean create(UserDTO userDTO);
    boolean update(UserDTO userDTO);
    boolean delete(String username);

}
