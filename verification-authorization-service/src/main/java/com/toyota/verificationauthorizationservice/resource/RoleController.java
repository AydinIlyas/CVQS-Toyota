package com.toyota.verificationauthorizationservice.resource;

import com.toyota.verificationauthorizationservice.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    @PostMapping
    public void add(@RequestBody Role role)
    {

    }
}
