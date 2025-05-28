package com.example.employeemanagement.config;

import com.example.employeemanagement.model.User;
import com.example.employeemanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

/**
 * Spring Security configuration class for the application.
 * Defines security rules, authentication providers, and password encoding.
 */
@Configuration // Marks this class as a source of bean definitions
@EnableWebSecurity // Enables Spring Security's web security support
@EnableMethodSecurity(prePostEnabled = true) // Enables method-level security annotations like @PreAuthorize
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final UserRepository userRepository; // Inject UserRepository to load user details

    /**
     * Constructor for SecurityConfig.
     *
     * @param userRepository The repository for User entities.
     */
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Defines the PasswordEncoder bean.
     * Uses BCryptPasswordEncoder for strong password hashing.
     *
     * @return An instance of BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the UserDetailsService bean.
     * This service is used by Spring Security to retrieve user details for authentication.
     * It fetches user information from your `UserRepository`.
     *
     * @return An implementation of UserDetailsService.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            logger.debug("Attempting to load user by username: {}", username);
            // Fetch user from your UserRepository
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        logger.warn("User not found: {}", username);
                        return new UsernameNotFoundException("User not found: " + username);
                    });
            // Convert your User entity to Spring Security's UserDetails object
            // Use org.springframework.security.core.userdetails.User
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole()) // Roles are prefixed with "ROLE_" by Spring Security by default if using hasRole()
                    .build();
        };
    }

    /**
     * Configures the AuthenticationManager bean.
     * This manager is used to perform authentication.
     *
     * @param authenticationConfiguration The AuthenticationConfiguration object.
     * @return An instance of AuthenticationManager.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures the SecurityFilterChain to define HTTP security rules.
     *
     * @param http The HttpSecurity object to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless REST APIs (consider alternatives for production)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/register").permitAll() // Allow public access to registration endpoint
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .httpBasic(httpBasic -> httpBasic.init(http)); // Enable Basic Authentication

        // Optional: Configure session management for stateless API
        // http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /**
     * CommandLineRunner bean to create an initial admin user if one doesn't exist.
     * This simulates "hardcoding" an admin user for initial setup.
     * It runs once after the application context is loaded.
     *
     * @param userRepository The UserRepository to save the admin user.
     * @param passwordEncoder The PasswordEncoder to hash the admin password.
     * @return A CommandLineRunner instance.
     */
    @Bean
    public CommandLineRunner createInitialAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if an ADMIN user already exists
            Optional<User> adminUser = userRepository.findByUsername("admin");
            if (adminUser.isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                // Hash the hardcoded password
                admin.setPassword(passwordEncoder.encode("adminpass")); // IMPORTANT: Change this default password immediately!
                admin.setRole("ADMIN"); // Assign ADMIN role
                userRepository.save(admin);
                logger.info("Initial ADMIN user 'admin' created with default password. PLEASE CHANGE IT!");
            } else {
                logger.info("Admin user 'admin' already exists.");
            }
        };
    }
}
