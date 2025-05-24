package nl.hilgenbos.psyichic_pies.configuration;

import nl.hilgenbos.psyichic_pies.entity.UserEntity;
import nl.hilgenbos.psyichic_pies.repository.UserRepository;
import nl.hilgenbos.psyichic_pies.security.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Value("${admin.username}")
    String adminUsername;

    @Value("${admin.password}")
    String adminPassword;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DatabaseInitializer(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Check if admin user already exists
        if (!userRepository.existsByUsername(adminUsername)) {
            // Generate a random seed
            byte[] seedBytes = new byte[16];
            new SecureRandom().nextBytes(seedBytes);
            String seed = Base64.getEncoder().encodeToString(seedBytes);

            // Create admin user
            UserEntity adminUser = new UserEntity();
            adminUser.setUsername(adminUsername);
            adminUser.setPasswordSeed(seed);
            adminUser.setPassword(passwordEncoder.encode(seed + adminPassword));
            adminUser.setRoles(List.of(Role.ADMIN));

            // Save admin user
            userRepository.save(adminUser);

            System.out.println("Default admin user created with username: admin and password: adminpie");
        }
    }
}
