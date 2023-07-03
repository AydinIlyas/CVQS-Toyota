package com.toyota.usermanagementservice.service.impl;

import com.toyota.usermanagementservice.dao.UserRepository;
import com.toyota.usermanagementservice.domain.Role;
import com.toyota.usermanagementservice.domain.User;
import com.toyota.usermanagementservice.dto.RegisterRequest;
import com.toyota.usermanagementservice.dto.UserDTO;
import com.toyota.usermanagementservice.dto.UserResponse;
import com.toyota.usermanagementservice.exception.*;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
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
        logger.info("Creating new User. Username: {}, Email: {}",userDTO.getUsername(),userDTO.getEmail());
        if (userRepository.existsByUsernameAndDeletedIsFalse(userDTO.getUsername())) {
            logger.warn("Username is already taken! Username: {}", userDTO.getUsername());
            throw new UserAlreadyExistsException("Username '" + userDTO.getUsername() + "' is already taken!");
        }

        if (userRepository.existsByEmailAndDeletedIsFalse(userDTO.getEmail())) {
            logger.warn("Email is already taken! Email: {}", userDTO.getEmail());
            throw new UserAlreadyExistsException("Email '" + userDTO.getEmail() + "' is already taken!");
        }
        User user = convertDtoToEntity(userDTO);
        Set<String> roles = userDTO.getRole().stream().map(Enum::toString).collect(Collectors.toSet());
        logger.debug("Sending request for creating user in verification-authorization-service!");
        Boolean response = webClientBuilder.build().post()
                .uri("http://verification-authorization-service/auth/register")
                .bodyValue(new RegisterRequest(userDTO.getUsername(), userDTO.getPassword(), roles))
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse->{
                    if(clientResponse.statusCode()==HttpStatus.CONFLICT)
                    {
                        logger.warn("User already exists in verification-authorization-service.");
                        throw new UserAlreadyExistsException("User already exists in verification-authorization-service");
                    }
                    else if(clientResponse.statusCode()==HttpStatus.BAD_REQUEST)
                    {
                        logger.warn("Problem with roles in verification-authorization-service");
                        throw new RoleNotFoundException("Problem with roles in verification-authorization-service");
                    }
                    else{
                        logger.warn("Unexpected exception in verification-authorization-service");
                        throw new UnexpectedException("Unexpected exception in verification-authorization-service");
                    }
                })
                .bodyToMono(Boolean.class)
                .block();
        if (response != null && !response) {
            logger.warn("Failed to create user! Reason: Unexpected problem in verification service");
            throw new UnexpectedException("Failed to create user! Reason: Unexpected problem in verification service");
        }
        User saved = userRepository.save(user);
        logger.info("User created successfully! Username:{}, Email: {}"
                ,saved.getUsername(),saved.getEmail());
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
    @Transactional
    public UserResponse update(HttpServletRequest request, Long userId, UserDTO userDTO) {
        logger.info("Updating user. User ID: {}",userId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (userDTO.getEmail() != null && !user.getEmail().equals(userDTO.getEmail())) {
                if(userRepository.existsByEmailAndDeletedIsFalse(userDTO.getEmail()))
                {
                    logger.warn("Email is already taken!");
                    throw new UserAlreadyExistsException("Email is already taken!");
                }
                user.setEmail(userDTO.getEmail());
                logger.debug("User email updated: {}",user.getEmail());
            }
            if (userDTO.getUsername() != null && !user.getUsername().equals(userDTO.getUsername())) {
                if(userRepository.existsByUsernameAndDeletedIsFalse(userDTO.getUsername()))
                {
                    logger.warn("Username is already taken!");
                    throw new UserAlreadyExistsException("Username is taken!");
                }
                String bearer = extractToken(request);
                logger.debug("Sending request for updating username in verification-authorization-service!");
                Boolean updated = webClientBuilder.build().put()
                        .uri("http://verification-authorization-service/auth/update/{oldUsername}",
                                user.getUsername())
                        .headers(auth -> auth.setBearerAuth(bearer))
                        .bodyValue(userDTO.getUsername())
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, response->{
                            if(response.statusCode()==HttpStatus.CONFLICT)
                            {
                                logger.warn("Username already exists in verification-authorization-service");
                                throw new UserAlreadyExistsException
                                        ("Username already exists in verification-authorization-service");
                            }
                            else if(response.statusCode()==HttpStatus.NOT_FOUND)
                            {
                                logger.warn("Username not found in verification-authorization-service.");
                                throw new UserNotFoundException
                                        ("Username not found in verification-authorization-service.");
                            }
                            else{
                                logger.warn("Unexpected exception in verification-authorization-service");
                                throw new UnexpectedException
                                        ("Unexpected exception in verification-authorization-service");
                            }
                        })
                        .bodyToMono(Boolean.class)
                        .block();
                if (updated != null && updated)
                {
                    user.setUsername(userDTO.getUsername());
                    logger.debug("User username updated: {}",user.getUsername());
                }
                else {
                    logger.warn("Unexpected Failure to change username in verification-authorization service!");
                    throw new UnexpectedException("Unexpected Failure to change username in verification-authorization service!");
                }
            }

            if (userDTO.getFirstname() != null && !user.getFirstname().equals(userDTO.getFirstname())) {
                user.setFirstname(userDTO.getFirstname());
                logger.debug("User firstname updated: {}",user.getFirstname());
            }
            if (userDTO.getLastname() != null && !user.getLastname().equals(userDTO.getLastname())) {
                user.setLastname(userDTO.getLastname());
                logger.debug("User lastname updated: {}",user.getLastname());
            }
            if (userDTO.getGender() != null && !user.getGender().equals(userDTO.getGender())) {
                user.setGender(userDTO.getGender());
                logger.debug("User Gender updated: {}",user.getGender());
            }
            User saved = userRepository.save(user);
            logger.info("User updated successfully! USER ID: {}, USERNAME: {}"
                    ,saved.getId(),saved.getUsername());
            return convertToResponse(saved);
        } else {
            logger.warn("User not found! ID: {}", userId);
            throw new UserNotFoundException("User not found! ID: " + userId);
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
        logger.info("Deleting user. User ID: {}",userId);
        String bearer = extractToken(request);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            logger.debug("Sending request for updating username in verification-authorization-service!");
            Boolean deleteFromAuth = webClientBuilder.build().put()
                    .uri("http://verification-authorization-service/auth/delete")
                    .headers(h -> h.setBearerAuth(bearer))
                    .bodyValue(user.getUsername())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response->{
                        if(response.statusCode()==HttpStatus.NOT_FOUND)
                        {
                            logger.warn("User not found in verification-authorization service");
                            throw new UserNotFoundException("User not found in verification-authorization service");
                        }
                        else{
                            logger.warn("Unexpected exception in verification service");
                            throw new UnexpectedException("Unexpected exception in verification service");
                        }
                    })
                    .bodyToMono(Boolean.class)
                    .block();
            if (deleteFromAuth != null && deleteFromAuth) {
                user.setDeleted(true);
                User saved = userRepository.save(user);
                logger.info("User deleted successfully! USER ID: {}, USERNAME: {}"
                        ,saved.getId(),saved.getUsername());
                return convertToResponse(saved);
            } else {
                logger.warn("User not found in verification-authorization-service! User ID: {}", userId);
                throw new UnexpectedException("User not found in verification-authorization-service! User ID: " + userId);
            }
        } else {
            logger.warn("User not found. ID: {}", userId);
            throw new UserNotFoundException("User not found. ID: " + userId);
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
        logger.info("Fetching users");
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
        Page<User> entities = userRepository.getUsersFiltered(firstname, lastname, email
                , username, pageable);
        logger.info("Fetched {} users",entities.getContent().size());
        return entities.map(this::convertToResponse);

    }

    /**
     * Adds new role to user
     *
     * @param userId User ID of the user who should get the new role
     * @param role   New role
     * @return UserResponse
     */
    @Override
    public UserResponse addRole(HttpServletRequest request, Long userId, Role role) {
        logger.info("Adding role to user. User ID: {}, New Role: {}",userId,role);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            String authToken = extractToken(request);
            User user = optionalUser.get();

            if (user.getRole().contains(role)) {
                logger.warn("User already has this role! Role: {}",role);
                throw new RoleAlreadyExistsException("User already has this Role. Role: " + role.toString());
            }
            logger.debug("Sending request for adding role to user in verification-authorization-service!");
            Boolean success = webClientBuilder.build()
                    .put()
                    .uri("http://verification-authorization-service/auth/addRole/{username}",
                            user.getUsername())
                    .headers(h -> h.setBearerAuth(authToken))
                    .bodyValue(role.toString())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->{
                        HttpHeaders headers=response.headers().asHttpHeaders();
                        String exceptionType=headers.getFirst("exception-type");
                        if(response.statusCode()==HttpStatus.NOT_FOUND
                                && Objects.equals(exceptionType, "RoleNotFound"))
                        {
                            logger.warn("Role not found in verification-authorization-service");
                            throw new RoleNotFoundException("Role not found in verification-authorization-service");
                        }
                        else if(response.statusCode()==HttpStatus.NOT_FOUND
                                && Objects.equals(exceptionType, "UserNotFound"))
                        {
                            logger.warn("User not found in verification-authorization-service.");
                            throw new UserNotFoundException("User not found in verification-authorization-service.");
                        }
                        else{
                            logger.warn("Unexpected exception in verification-authorization-service");
                            throw new UnexpectedException("Unexpected exception in verification-authorization-service");
                        }
                    } )
                    .bodyToMono(Boolean.class)
                    .block();
            if (success != null && success) {
                user.getRole().add(role);
                userRepository.save(user);
                logger.info(" Added new role to user. Username: {}, Role: {}",
                        user.getUsername(), role);
                return convertToResponse(user);
            } else {
                throw new UnexpectedException("Failed to add role in verification service!");
            }

        } else {
            logger.warn("User not found! ID: {}", userId);
            throw new UserNotFoundException("User not found! ID: " + userId);
        }

    }

    /**
     * Adds new role to user
     *
     * @param userId User ID of the user who is to lose the existing role
     * @param role   Role to remove
     * @return UserResponse
     */
    @Override
    public UserResponse removeRole(HttpServletRequest request, Long userId, Role role) {
        logger.info("Adding role to user. User ID: {}, New Role: {}",userId,role);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            String authToken = extractToken(request);
            User user = optionalUser.get();
            if (user.getRole().size() <= 1) {
                logger.warn("User only have single role! Username: {}",user.getUsername());
                throw new SingleRoleRemovalException();
            }
            if (!user.getRole().contains(role)) {
                logger.warn("The user does not own this role! Role: {}", role.toString());
                throw new RoleNotFoundException("The user does not own this role! Role: " + role);
            }
            logger.debug("Sending request for removing role from user in verification-authorization-service!");
            Boolean success = webClientBuilder.build()
                    .put()
                    .uri("http://verification-authorization-service/auth/removeRole/{username}",
                            user.getUsername())
                    .headers(h -> h.setBearerAuth(authToken))
                    .bodyValue(role.toString())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->{
                        HttpHeaders headers=response.headers().asHttpHeaders();
                        String exceptionType=headers.getFirst("exception-type");
                        if(response.statusCode()==HttpStatus.NOT_FOUND
                                && Objects.equals(exceptionType, "RoleNotFound"))
                        {
                            throw new RoleNotFoundException("Role not found in verification-authorization-service");
                        }
                        else if(response.statusCode()==HttpStatus.NOT_FOUND
                                && Objects.equals(exceptionType, "UserNotFound"))
                        {
                            throw new UserNotFoundException("User not found in verification-authorization-service");
                        }
                        else{
                            throw new UnexpectedException("Unexpected exception in verification-authorization-service");
                        }
                    } )
                    .bodyToMono(Boolean.class)
                    .block();
            if (success != null && success) {
                user.getRole().remove(role);
                userRepository.save(user);
                logger.info("Removed role from user successfully. USER ID: {}, USERNAME: {}, ROLE: {},",
                        user.getId(), user.getUsername(), role);
                return convertToResponse(user);
            } else {
                logger.warn("Unexpected exception while removing role in verification service!");
                throw new UnexpectedException("Remove Role failed in verification Service!");
            }

        } else {
            logger.warn("User not found! ID: {}", userId);
            throw new UserNotFoundException("User not found! ID: " + userId);
        }

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
        throw new BearerTokenNotFoundException("Bearer token not found");
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
