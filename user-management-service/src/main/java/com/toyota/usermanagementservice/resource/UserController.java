package com.toyota.usermanagementservice.resource;

import com.toyota.usermanagementservice.dto.UserDTO;
import com.toyota.usermanagementservice.service.abstracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-management")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
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
}
