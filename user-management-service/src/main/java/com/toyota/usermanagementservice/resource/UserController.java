package com.toyota.usermanagementservice.resource;

import com.toyota.usermanagementservice.dto.UserDTO;
import com.toyota.usermanagementservice.dto.UserResponse;
import com.toyota.usermanagementservice.service.abstracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-management")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

//    @GetMapping("/list")
//    public List<UserResponse> getAll(
//            @RequestParam(value="attribute",required = false)String attribute,
//            @RequestParam(value="page",defaultValue = "0")int page,
//            @RequestParam(value="size",defaultValue = "5")int size,
//            @RequestParam(value="desired",required = false)String desired,
//            @RequestParam(value="direction",defaultValue = "asc")String direction,
//            @RequestParam(value="sortField",defaultValue = "id")String sortField
//
//    )
//    {
//        return userService.getAll(page,size,attribute,desired,direction,sortField);
//    }
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
