package com.toyota.usermanagementservice.service.impl;

import com.toyota.usermanagementservice.dao.UserRepository;
import com.toyota.usermanagementservice.domain.User;
import com.toyota.usermanagementservice.dto.RegisterRequest;
import com.toyota.usermanagementservice.dto.UserDTO;
import com.toyota.usermanagementservice.dto.UserResponse;
import com.toyota.usermanagementservice.exception.UnexpectedException;
import com.toyota.usermanagementservice.exception.UserAlreadyExistsException;
import com.toyota.usermanagementservice.exception.UserNotFoundException;
import com.toyota.usermanagementservice.service.abstracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final WebClient.Builder webClientBuilder;
    private final ModelMapper modelMapper;
    private final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    /**
     * Creates new user
     *
     * @param userDTO User to be created
     * @return UserResponse of saved new user.
     */
    @Override
    public UserResponse create(UserDTO userDTO) {

        if(userRepository.existsByUsername(userDTO.getUsername()))
        {
            logger.warn("USERNAME IS ALREADY TAKEN! USERNAME: {}",userDTO.getUsername());
            throw new UserAlreadyExistsException("Username '"+userDTO.getUsername()+"' is already taken!");
        }

        if(userRepository.existsByEmail(userDTO.getEmail()))
        {
            logger.warn("EMAIL IS ALREADY TAKEN! EMAIL: {}",userDTO.getEmail());
            throw new UserAlreadyExistsException("EMAIL '"+userDTO.getEmail()+"' is already taken!");
        }
        User user = convertDtoToEntity(userDTO);
        Set<String> roles = userDTO.getRole().stream().map(role -> role.toString()).collect(Collectors.toSet());
        Boolean response = webClientBuilder.build().post()
                .uri("http://verification-authorization-service/auth/register")
                .bodyValue(new RegisterRequest(userDTO.getUsername(), userDTO.getPassword(), roles))
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        if (response != null && !response) {
            logger.warn("USER COULDN'T CREATED!");
            throw new UnexpectedException("USER COULDN'T CREATED!");
        }
        User saved = userRepository.save(user);
        logger.info("USER SUCCESSFULLY CREATED! ID: {}", saved);
        return convertToResponse(saved);
    }

    /**
     * Updates user credentials.
     *
     * @param request HttpServletRequest for sending bearer to verification if required.
     * @param userId  ID of user to be updated.
     * @param userDTO Updated user information.
     * @return UserResponse of updated user.
     */
    @Override
    public UserResponse update(HttpServletRequest request, Long userId, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (userDTO.getUsername()!=null&&!user.getUsername().equals(userDTO.getUsername())) {
                String bearer = extractToken(request);
                if (bearer == null) {
                    return null;
                }
                Boolean updated = webClientBuilder.build().put()
                        .uri("http://verification-authorization-service/auth/update/{oldUsername}", user.getUsername())
                        .headers(auth -> auth.setBearerAuth(bearer))
                        .bodyValue(userDTO.getUsername())
                        .retrieve()
                        .bodyToMono(Boolean.class).block();
                if(updated!=null&&updated)
                user.setUsername(userDTO.getUsername());
                else{
                    logger.warn("Username couldn't changed in verification service!");
                    throw new UnexpectedException("Username couldn't changed in verification service!");
                }
            }
            if (userDTO.getEmail()!=null&&!user.getEmail().equals(userDTO.getEmail())) {
                user.setEmail(userDTO.getEmail());
            }
            if (userDTO.getFirstname()!=null&&!user.getFirstname().equals(userDTO.getFirstname())) {
                user.setFirstname(userDTO.getFirstname());
            }
            if (userDTO.getLastname()!=null&&!user.getLastname().equals(userDTO.getLastname())) {
                user.setLastname(userDTO.getLastname());
            }
            if (userDTO.getGender()!=null&&!user.getGender().equals(userDTO.getGender())) {
                user.setGender(userDTO.getGender());
            }
            User saved = userRepository.save(user);

            return convertToResponse(saved);
        } else {
            logger.warn("USER NOT FOUND! ID: {}", userId);
            throw new UserNotFoundException("USER NOT FOUND! ID: " + userId);
        }
    }


    /**
     * Soft deletes user.
     *
     * @param request HttpServletRequest for sending request to verification service if required.
     * @param userId  ID of user to be updated.
     * @return UserResponse of soft deleted user.
     */
    @Override
    public UserResponse deleteUser(HttpServletRequest request, Long userId) {
        String bearer = extractToken(request);
        if(bearer==null)
            return null;
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Boolean deleteFromAuth = webClientBuilder.build().put()
                    .uri("http://verification-authorization-service/auth/delete")
                    .headers(h -> h.setBearerAuth(bearer))
                    .bodyValue(user.getUsername())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
            if (deleteFromAuth != null && deleteFromAuth) {
                user.setDeleted(true);
                User saved = userRepository.save(user);
                return convertToResponse(saved);
            } else {
                logger.warn("USER COULDN'T FOUND IN AUTHENTICATION SERVICE! ID: {}", userId);
                throw new UnexpectedException("USER COULDN'T FOUND IN AUTHENTICATION SERVICE! ID: " + userId);
            }
        } else {
            logger.warn("USER NOT FOUND. Id: {}", userId);
            throw new UserNotFoundException("USER NOT FOUND. Id: " + userId);
        }
    }


    /**
     * Lists user with paging, filtering and sorting.
     *
     * @param firstname Filter for firstname.
     * @param lastname  Filter for lastname.
     * @param username  Filter for username.
     * @param email     Filter for email.
     * @param page      Page to be displayed
     * @param size      Size of the page
     * @param sortList  Which fields to sort by
     * @param sortOrder Sorting direction (ASC/DESC)
     * @return Page of UserResponses
     */
    @Override
    public Page<UserResponse> getAll(String firstname, String lastname,
                                     String username, String email,
                                     int page, int size, List<String> sortList,
                                     String sortOrder) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
        Page<User> entities = userRepository.getUsersFiltered(firstname, lastname, email
                , username, pageable);
        return entities.map(this::convertToResponse);

    }

    /**
     * Creates sorting rules
     *
     * @param sortList      List of which fields to sort by
     * @param sortDirection direction of sorting (ASC/DESC)
     * @return List<Sort.Order>
     */
    private List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction;
        for (String sort : sortList) {
            if (sortDirection.equalsIgnoreCase("DESC")) {
                direction = Sort.Direction.DESC;
            } else {
                direction = Sort.Direction.ASC;
            }
            sorts.add(new Sort.Order(direction, sort));
        }
        return sorts;
    }


    /**
     * @param request HttpServletRequest for extract token from it.
     * @return Bearer Token
     */
    private String extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    /**
     * Converts entity to response
     *
     * @param user Entity to be converted to response
     * @return UserResponse
     */
    private UserResponse convertToResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    /**
     * Converts DTO to Entity
     *
     * @param userDTO DTO to be converted to entity
     * @return User
     */
    private User convertDtoToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
