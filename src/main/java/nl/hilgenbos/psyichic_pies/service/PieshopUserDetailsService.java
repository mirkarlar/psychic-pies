package nl.hilgenbos.psyichic_pies.service;

import nl.hilgenbos.psyichic_pies.entity.UserEntity;
import nl.hilgenbos.psyichic_pies.repository.UserRepository;
import nl.hilgenbos.psyichic_pies.security.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PieshopUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(PieshopUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user details for username: {}", username);
        logger.debug("Querying user repository for username: {}", username);

        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            logger.warn("User not found with username: {}", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        logger.debug("User found, extracting role names");
        String[] roleNames = getRoleNamesFromUser(userEntity);
        logger.debug("User has the following roles: {}", (Object) roleNames);

        logger.info("Successfully loaded user details for: {}", username);
        return User.withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(roleNames)
                .build();
    }

    private String[] getRoleNamesFromUser(UserEntity userEntity) {
        // Extract role names from Role objects
        logger.debug("Extracting role names from user: {}", userEntity.getUsername());
        String[] roleNames = userEntity.getRoles().stream()
                .map(Role::getRoleName)
                .toArray(String[]::new);
        logger.debug("Extracted {} roles for user", roleNames.length);
        return roleNames;
    }
}
