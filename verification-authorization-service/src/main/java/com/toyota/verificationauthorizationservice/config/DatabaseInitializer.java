package com.toyota.verificationauthorizationservice.config;

import com.toyota.verificationauthorizationservice.dao.RoleRepository;
import com.toyota.verificationauthorizationservice.dao.UserRepository;
import com.toyota.verificationauthorizationservice.domain.Role;
import com.toyota.verificationauthorizationservice.dto.RegisterRequest;
import com.toyota.verificationauthorizationservice.service.abstracts.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Class for adding data to database
 */
@Component
public class DatabaseInitializer {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public DatabaseInitializer(RoleRepository roleRepository, UserRepository userRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Autowired

    @PostConstruct
    public void initialize()
    {
        List<Role> allRoles=roleRepository.findAll();
        if(allRoles.size()==0)
        {
            List<Role> roles=List.of(
                    new Role(1L,"ADMIN","ADMIN",null),
                    new Role(2L,"LEADER","LEADER",null),
                    new Role(3L,"OPERATOR","OPERATOR",null)
            );
            roleRepository.saveAll(roles);
        }
        if(!userRepository.existsByUsernameAndDeletedIsFalse("admin"))
        {
            RegisterRequest request=new RegisterRequest("admin","admin",
                    Set.of("ADMIN","LEADER","OPERATOR"));
            userService.register(request);
        }
    }
}
