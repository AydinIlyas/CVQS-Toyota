package com.toyota.usermanagementservice.service.abstracts;

import com.toyota.usermanagementservice.domain.Role;
import com.toyota.usermanagementservice.dto.UserDTO;
import com.toyota.usermanagementservice.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    /**
     * Creates new user
     *
     * @param userDTO User to be created
     * @return UserResponse of saved new user.
     */
    UserResponse create(UserDTO userDTO);
    /**
     * Updates user
     *
     * @param userId  ID of user to be updated.
     * @param userDTO Updated user information.
     * @return UserResponse of updated user.
     */
    UserResponse update(Long userId,UserDTO userDTO);
    /**
     * Soft deletes user.
     *
     * @param userId  ID of user to be updated.
     * @return UserResponse of soft deleted user.
     */
    UserResponse deleteUser(Long userId);
    /**
     * Lists user with paging, filtering and sorting.
     *
     * @param firstname Filter for firstname.
     * @param lastname  Filter for lastname.
     * @param username  Filter for username.
     * @param email     Filter for email.
     * @param page      Page to be displayed
     * @param size      Size of the page
     * @param sortList  Which fields to sort by
     * @param sortOrder Sorting direction (ASC/DESC)
     * @return Page of UserResponses
     */

    Page<UserResponse> getAll(String firstname, String lastname, String username, String email,
                              int page, int size, List<String> sortList, String sortOrder);
    /**
     * Adds new role to user
     *
     * @param userId User ID of the user who should get the new role
     * @param role   New role
     * @return UserResponse
     */
    UserResponse addRole(Long userId,Role role);
    /**
     * Removes role from user
     *
     * @param userId User ID of the user who is to lose the existing role
     * @param role   Role to remove
     * @return UserResponse
     */
    UserResponse removeRole(Long userId, Role role);
}
