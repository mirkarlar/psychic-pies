package nl.hilgenbos.psyichic_pies.service;

import nl.hilgenbos.psyichic_pies.dto.CreateUserRequestDto;
import nl.hilgenbos.psyichic_pies.dto.LoginRequestDto;
import nl.hilgenbos.psyichic_pies.dto.LoginResponseDto;
import nl.hilgenbos.psyichic_pies.entity.UserEntity;
import nl.hilgenbos.psyichic_pies.repository.UserRepository;
import nl.hilgenbos.psyichic_pies.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponseDto login(LoginRequestDto loginRequest) throws AuthenticationException {
        logger.info("Processing login request for user: {}", loginRequest.username());
        logger.debug("Attempting to find user in repository");

        try {
            UserEntity user = userRepository.findOptionalByUsername(loginRequest.username()).orElseThrow(() ->
                    new AuthenticationException("User not found"));

            logger.debug("User found, verifying password");
            String seed = user.getPasswordSeed();
            if (seed != null && bCryptPasswordEncoder.matches(seed + loginRequest.password(), user.getPassword())) {
                logger.debug("Password verified, generating JWT token");
                LoginResponseDto response;
                response = new LoginResponseDto(jwtTokenProvider.generateToken(user.getUsername(), user.getRoles()));
                logger.info("Login successful for user: {}", loginRequest.username());
                return response;
            } else {
                logger.warn("Invalid password for user: {}", loginRequest.username());
                throw new AuthenticationException("Invalid username/password");
            }
        } catch (AuthenticationException e) {
            logger.warn("Authentication failed: {}", e.getMessage());
            throw e;
        }
    }

    public UserEntity createUser(CreateUserRequestDto createUserRequest) {
        logger.info("Creating new user with username: {}", createUserRequest.username());

        // Check if username already exists
        logger.debug("Checking if username already exists");
        if (userRepository.existsByUsername(createUserRequest.username())) {
            logger.warn("Username already exists: {}", createUserRequest.username());
            throw new IllegalArgumentException("Username already exists");
        }

        // Generate a random seed
        logger.debug("Generating random seed for password encryption");
        byte[] seedBytes = new byte[16];
        new SecureRandom().nextBytes(seedBytes);
        String seed = Base64.getEncoder().encodeToString(seedBytes);

        // Create new user entity
        logger.debug("Creating new user entity with roles: {}", createUserRequest.roles());
        UserEntity newUser = new UserEntity();
        newUser.setUsername(createUserRequest.username());
        newUser.setPasswordSeed(seed);
        newUser.setPassword(bCryptPasswordEncoder.encode(seed + createUserRequest.password()));
        newUser.setRoles(createUserRequest.roles());

        // Save and return the new user
        logger.debug("Saving new user to repository");
        UserEntity savedUser = userRepository.save(newUser);
        logger.info("Successfully created user with ID: {}", savedUser.getId());
        return savedUser;
    }
}
