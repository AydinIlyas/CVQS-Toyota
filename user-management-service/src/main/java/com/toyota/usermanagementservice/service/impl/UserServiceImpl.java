package com.toyota.usermanagementservice.service.impl;

import com.toyota.usermanagementservice.dao.UserRepository;
import com.toyota.usermanagementservice.domain.User;
import com.toyota.usermanagementservice.dto.RegisterRequest;
import com.toyota.usermanagementservice.dto.UserDTO;
import com.toyota.usermanagementservice.service.abstracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final WebClient.Builder webClientBuilder;

    /**
     * @param userDTO
     * @return
     */
    @Override
    public boolean create(UserDTO userDTO) {
        try {
            User user = User.builder()
                    .firstname(userDTO.getFirstname())
                    .lastname(userDTO.getLastname())
                    .email(userDTO.getEmail())
                    .username(userDTO.getUsername())
                    .role(userDTO.getRole())
                    .gender(userDTO.getGender())
                    .build();
            Set<String> roles=userDTO.getRole().stream().map(role -> role.toString()).collect(Collectors.toSet());
            Boolean response=webClientBuilder.build().post()
                    .uri("http://verification-authorization-service/auth/register")
                    .bodyValue(new RegisterRequest(userDTO.getUsername(), userDTO.getPassword(),roles))
                            .retrieve()
                    .bodyToMono(Boolean.class)
                                            .block();
            if(response==null||!response)return false;

            userRepository.save(user);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * @param userDTO
     * @return
     */
    @Override
    public boolean update(UserDTO userDTO) {
        return false;
    }

    /**
     * @param username
     * @return
     */
    @Override
    public boolean delete(String username) {
        return false;
    }
}
