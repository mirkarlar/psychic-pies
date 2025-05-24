package nl.hilgenbos.psyichic_pies.controller;

import nl.hilgenbos.psyichic_pies.dto.CreateUserRequestDto;
import nl.hilgenbos.psyichic_pies.dto.LoginRequestDto;
import nl.hilgenbos.psyichic_pies.dto.LoginResponseDto;
import nl.hilgenbos.psyichic_pies.entity.UserEntity;
import nl.hilgenbos.psyichic_pies.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.sasl.AuthenticationException;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) throws AuthenticationException {
        logger.info("Login attempt for user: {}", loginRequestDto.username());
        logger.debug("Processing login request");
        try {
            LoginResponseDto response = userService.login(loginRequestDto);
            logger.info("User {} successfully logged in", loginRequestDto.username());
            return response;
        } catch (AuthenticationException e) {
            logger.warn("Failed login attempt for user: {}", loginRequestDto.username());
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        logger.info("Attempting to create new user with username: {}", createUserRequestDto.username());
        logger.debug("User creation request with roles: {}", createUserRequestDto.roles());
        try {
            UserEntity createdUser = userService.createUser(createUserRequestDto);
            logger.info("Successfully created user with ID: {}", createdUser.getId());
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to create user: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
