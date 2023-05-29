package com.toyota.usermanagementservice.service.impl;

import com.toyota.usermanagementservice.dao.UserRepository;
import com.toyota.usermanagementservice.domain.User;
import com.toyota.usermanagementservice.dto.RegisterRequest;
import com.toyota.usermanagementservice.dto.UserDTO;
import com.toyota.usermanagementservice.dto.UserResponse;
import com.toyota.usermanagementservice.exception.UnexpectedException;
import com.toyota.usermanagementservice.exception.UserNotFoundException;
import com.toyota.usermanagementservice.service.abstracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final WebClient.Builder webClientBuilder;

    /**
     * @param userDTO User to create
     * @return boolean
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
            Set<String> roles = userDTO.getRole().stream().map(role -> role.toString()).collect(Collectors.toSet());
            Boolean response = webClientBuilder.build().post()
                    .uri("http://verification-authorization-service/auth/register")
                    .bodyValue(new RegisterRequest(userDTO.getUsername(), userDTO.getPassword(), roles))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            if (response == null || !response) return false;

            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param request
     * @param userId
     * @param userDTO
     * @return
     */
    @Override
    public UserResponse update(HttpServletRequest request, Long userId, UserDTO userDTO) {
        User user=userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("USER NOT FOUND! Id: "+userId));
        if(!user.getUsername().equals(userDTO.getUsername()))
        {
            String bearer=extractToken(request);
            if(bearer==null)
            {
                return null;
            }
            webClientBuilder.build().put()
                    .uri("http://verification-authorization-service/auth/update/{oldUsername}",user.getUsername())
                    .headers(auth->auth.setBearerAuth(bearer))
                    .bodyValue(userDTO.getUsername())
                    .retrieve()
                    .bodyToMono(Boolean.class).block();
            user.setUsername(userDTO.getUsername());
        }
        if(!user.getEmail().equals(userDTO.getEmail()))
        {
            user.setEmail(userDTO.getEmail());
        }
        if(!user.getFirstname().equals(userDTO.getFirstname()))
        {
            user.setFirstname(userDTO.getFirstname());
        }
        if(!user.getLastname().equals(userDTO.getLastname()))
        {
            user.setLastname(userDTO.getLastname());
        }
        if(!user.getGender().equals(userDTO.getGender()))
        {
            user.setGender(userDTO.getGender());
        }
        userRepository.save(user);

        return UserResponse.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .gender(user.getGender())
                .build();
    }


    /**
     * Soft deletes user
     *
     * @param userId Id of user which should be deleted.
     * @return UserResponse
     */
    @Override
    public UserResponse deleteUser(HttpServletRequest request,Long userId) {
        String bearer=extractToken(request);
        if(bearer==null)return null;
        User user = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException("USER NOT FOUND. Id: " + userId));

        Boolean deleteFromAuth=webClientBuilder.build().put()
                .uri("http://verification-authorization-service/auth/delete")
                .headers(h->h.setBearerAuth(bearer))
                .bodyValue(user.getUsername())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        if(deleteFromAuth!=null&&deleteFromAuth)
        {
            user.setDeleted(true);
            userRepository.save(user);
        }
        else
            throw new UnexpectedException("USER COULDN'T FOUND IN AUTHENTICATION SERVICE!");

        return UserResponse.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .gender(user.getGender())
                .build();
    }

    /**
     * @param filterFirstname
     * @param filterLastname
     * @param filterUsername
     * @param filterEmail
     * @param page
     * @param size
     * @param sortList
     * @param sortOrder
     * @return
     */
    @Override
    public Page<UserResponse> getAll(String filterFirstname, String filterLastname,
                                     String filterUsername, String filterEmail,
                                     int page, int size, List<String> sortList,
                                     String sortOrder) {
        // create Pageable object using the page, size and sort details
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
        // fetch the page object by additionally passing pageable with the filters
        Page<User> entities=userRepository.getUsersFiltered(filterFirstname,filterLastname,filterEmail
                ,filterUsername,pageable);
        return entities.map(this::convertToResponse);

    }
    private List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction;
        for (String sort : sortList) {
            if (sortDirection != null) {
                direction = Sort.Direction.fromString(sortDirection);
            } else {
                direction = Sort.Direction.DESC;
            }
            sorts.add(new Sort.Order(direction, sort));
        }
        return sorts;
    }


    /**
     * @param request For extracting the token.
     * @return  Bearer Token
     */
    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
    private UserResponse convertToResponse(User user)
    {
        return UserResponse.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .gender(user.getGender())
                .build();
    }
}
