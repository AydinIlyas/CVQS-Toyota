package com.toyota.verificationauthorizationservice.resource;

import com.toyota.verificationauthorizationservice.dto.AuthenticationRequest;
import com.toyota.verificationauthorizationservice.dto.AuthenticationResponse;
import com.toyota.verificationauthorizationservice.service.abstracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request)
    {
        AuthenticationResponse response=userService.register(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request)
    {
        AuthenticationResponse response=userService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public boolean verify()
    {
        int a=10;
        if(a+1==11)
        {
            return true;
        }
        return false;
//        return userService.verify(authHeader);
    }
}
