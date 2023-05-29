package com.toyota.usermanagementservice.resource;

import com.toyota.usermanagementservice.dto.UserDTO;
import com.toyota.usermanagementservice.dto.UserResponse;
import com.toyota.usermanagementservice.service.abstracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-management")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/list")
    public Page<UserResponse> getAll(
            @RequestParam(defaultValue = "")String filterFirstname,
            @RequestParam(defaultValue = "")String filterLastname,
            @RequestParam(defaultValue = "")String filterUsername,
            @RequestParam(defaultValue = "")String filterEmail,
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "5")int size,
            @RequestParam(defaultValue = "") List<String> sortList,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortOrder

    )
    {
        return userService.getAll(filterFirstname,filterLastname,filterUsername
                ,filterEmail,page,size,sortList,sortOrder.toString());
    }
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody UserDTO userDTO)
    {
        boolean success=userService.create(userDTO);
        if(success)
        {
            return ResponseEntity.ok("It worked");
        }
        else{
            return ResponseEntity.ok("It didn't work");
        }
    }

    @PutMapping("update/{user_id}")
    public UserResponse update (HttpServletRequest request,@PathVariable("user_id") Long user_id,@RequestBody UserDTO userDTO)
    {
        return userService.update(request,user_id,userDTO);
    }
    @PutMapping("/delete")
    public UserResponse deleteUser(@RequestBody Long userId, HttpServletRequest request)
    {
        return userService.deleteUser(request,userId);
    }

}
