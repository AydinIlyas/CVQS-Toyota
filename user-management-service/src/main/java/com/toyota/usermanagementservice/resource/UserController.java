package com.toyota.usermanagementservice.resource;

import com.toyota.usermanagementservice.domain.Role;
import com.toyota.usermanagementservice.dto.UserDTO;
import com.toyota.usermanagementservice.dto.UserResponse;
import com.toyota.usermanagementservice.service.abstracts.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for user related requests.
 */
@RestController
@RequestMapping("/user-management")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Lists user with paging, filtering and sorting.
     * @param firstname Filter for firstname.
     * @param lastname Filter for lastname.
     * @param username Filter for username.
     * @param email Filter for email.
     * @param page Page to be displayed
     * @param size Size of the page
     * @param sortList Which fields to sort by
     * @param sortOrder Sorting direction (ASC/DESC)
     * @return Page of UserResponses
     */
    @GetMapping("/users")
    public Page<UserResponse> getAll(
            @RequestParam(defaultValue = "")String firstname,
            @RequestParam(defaultValue = "")String lastname,
            @RequestParam(defaultValue = "")String username,
            @RequestParam(defaultValue = "")String email,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "5")int size,
            @RequestParam(defaultValue = "") List<String> sortList,
            @RequestParam(defaultValue = "ASC") String sortOrder

    )
    {
        return userService.getAll(firstname, lastname, username
                , email,page,size,sortList,sortOrder);
    }

    /**
     * Creates new user.
     * @param userDTO user that should be created.
     * @return UserResponse of created user
     */
    @PostMapping("/create")
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserDTO userDTO)
    {
        UserResponse response=userService.create(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Updates user
     * @param user_id ID of user to be updated
     * @param userDTO Updated userDTO
     * @return ResponseEntity with updated User.
     */
    @PutMapping("update/{user_id}")
    public ResponseEntity<UserResponse> update (@PathVariable("user_id") Long user_id,
                                                @RequestBody UserDTO userDTO)
    {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(user_id,userDTO));
    }

    /**
     * Adds role to user
     * @param user_id ID of user
     * @param role Role to be added
     * @return ResponseEntity with UserResponse
     */
    @PutMapping("/role/add/{user_id}")
    public ResponseEntity<UserResponse> addRole(@PathVariable("user_id") Long user_id,
                                                @RequestBody Role role)
    {
        UserResponse response=userService.addRole(user_id,role);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Removes role from user
     * @param user_id ID of user
     * @param role Role to be removed
     * @return ResponseEntity with UserResponse
     */
    @PutMapping("/role/remove/{user_id}")
    public ResponseEntity<UserResponse> removeRole(@PathVariable("user_id") Long user_id,
                                                   @RequestBody Role role)
    {
        UserResponse response=userService.removeRole(user_id,role);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Deletes User
     * @param userId ID of user
     * @return UserResponse of deleted user
     */
    @PutMapping("/delete")
    public ResponseEntity<UserResponse> deleteUser(@RequestBody Long userId)
    {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(userId));
    }

}
