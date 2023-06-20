package com.toyota.usermanagementservice.resource;

import com.toyota.usermanagementservice.domain.Role;
import com.toyota.usermanagementservice.dto.UserDTO;
import com.toyota.usermanagementservice.dto.UserResponse;
import com.toyota.usermanagementservice.service.abstracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    @GetMapping("/list")
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

    @PutMapping("update/{user_id}")
    public ResponseEntity<UserResponse> update (HttpServletRequest request,
                                @PathVariable("user_id") Long user_id,
                                @RequestBody UserDTO userDTO)
    {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(request,user_id,userDTO));
    }
    @PutMapping("/role/add/{user_id}")
    public ResponseEntity<UserResponse> addRole(HttpServletRequest request,
                                                @PathVariable("user_id") Long user_id,
                                                @RequestBody Role role)
    {
        UserResponse response=userService.addRole(request,user_id,role);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PutMapping("/role/remove/{user_id}")
    public ResponseEntity<UserResponse> removeRole(HttpServletRequest request,
                                                @PathVariable("user_id") Long user_id,
                                                @RequestBody Role role)
    {
        UserResponse response=userService.removeRole(request,user_id,role);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PutMapping("/delete")
    public ResponseEntity<UserResponse> deleteUser(HttpServletRequest request,@RequestBody Long userId)
    {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(request,userId));
    }

}
