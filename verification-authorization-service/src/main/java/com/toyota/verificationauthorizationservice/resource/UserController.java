package com.toyota.verificationauthorizationservice.resource;

import com.toyota.verificationauthorizationservice.dto.AuthenticationRequest;
import com.toyota.verificationauthorizationservice.dto.AuthenticationResponse;
import com.toyota.verificationauthorizationservice.dto.PasswordsDTO;
import com.toyota.verificationauthorizationservice.dto.RegisterRequest;
import com.toyota.verificationauthorizationservice.service.abstracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Controller for handling authentication related requests.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Calls UserService registering function.
     * @param request Request for registering.
     * @return Boolean
     */
    @PostMapping("/register")
    public Boolean register(@RequestBody RegisterRequest request)
    {
        AuthenticationResponse response=userService.register(request);
        if(response==null)return false;
        else return true;
    }

    /**
     * Calls user service login function.
     * @param request Login Request
     * @return ResponseEntity of token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request)
    {
        AuthenticationResponse response=userService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Updating username
     * @param newUsername New username
     * @param oldUsername Old username for finding entity
     * @return Boolean
     */
    @PutMapping("update/{oldUsername}")
    public Boolean update(@RequestBody String newUsername,
                          @PathVariable("oldUsername") String oldUsername)
    {
        return userService.updateUsername(newUsername,oldUsername);
    }

    @PutMapping("/changePassword")
    public ResponseEntity<Entity> changePassword(HttpServletRequest request,@RequestBody PasswordsDTO passwordsDTO)
    {
        boolean success=userService.changePassword(request,passwordsDTO);
        if(success)
            return ResponseEntity.status(HttpStatus.OK).build();
        else{
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    /**
     * Verifies user bearer token
     * @param request Request
     * @return Set of permissions of the user
     */
    @GetMapping("/verify")
    public Set<String> verify(HttpServletRequest request)
    {
        return userService.verify(request);
    }

    /**
     * Verifies user bearer token
     * @param request Request
     * @return Map of permissions and username
     */
    @GetMapping("/verifyAndUsername")
    public Map<String,String> verifyAndUsername(HttpServletRequest request)
    {
        return userService.verifyAndUsername(request);
    }


    /**
     * Soft deletes user
     * @param username Username of user which will be deleted.
     * @return Boolean
     */
    @PutMapping("/delete")
    public Boolean delete(@RequestBody String username)
    {
        return userService.delete(username);
    }


}