package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.UserRequestDTO; // Changed import
import com.example.employeemanagement.dto.UserResponseDTO; // New import
import com.example.employeemanagement.model.User;
import com.example.employeemanagement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing User-related business logic.
 * Handles user registration, retrieval, and password encoding.
 */
@Service // Marks this class as a Spring Service component
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class); // Logger for this class

    private final UserRepository userRepository; // Inject UserRepository for database operations
    private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder for hashing passwords

    /**
     * Constructor for UserService.
     * Spring automatically injects UserRepository and PasswordEncoder.
     *
     * @param userRepository The repository for User entities.
     * @param passwordEncoder The password encoder.
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user.
     * Performs checks for existing username/email and encodes the password.
     * By default, newly registered users are assigned the "NORMAL_USER" role.
     *
     * @param userRequestDTO The UserRequestDTO containing user registration details.
     * @return The saved User entity.
     * @throws IllegalArgumentException if username or email already exists.
     */
    @Transactional // Ensures the entire method runs within a single database transaction
    public User registerUser(UserRequestDTO userRequestDTO) { // Changed parameter type
        logger.info("Attempting to register new user with username: {}", userRequestDTO.getUsername());

        // Check if username already exists
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            logger.warn("User registration failed: Username '{}' already exists.", userRequestDTO.getUsername());
            throw new IllegalArgumentException("Username already exists.");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            logger.warn("User registration failed: Email '{}' already exists.", userRequestDTO.getEmail());
            throw new IllegalArgumentException("Email already exists.");
        }

        // Create a new User entity from DTO
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        // Encode the password before saving
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        // Assign default role for new registrations
        user.setRole("NORMAL_USER"); // As per requirement, user cannot specify role during registration

        User savedUser = userRepository.save(user); // Save the user to the database
        logger.info("Successfully registered new user with ID: {}", savedUser.getId());
        return savedUser;
    }

    /**
     * Finds a user by their username.
     * Used primarily by Spring Security for authentication.
     *
     * @param username The username to search for.
     * @return An Optional containing the User if found, or empty if not found.
     */
    public Optional<User> findByUsername(String username) {
        logger.debug("Attempting to find user by username: {}", username);
        return userRepository.findByUsername(username);
    }

    /**
     * Finds a user by their ID and converts it to UserResponseDTO.
     *
     * @param id The ID of the user to search for.
     * @return An Optional containing the UserResponseDTO if found.
     */
    public Optional<UserResponseDTO> findUserResponseById(Long id) {
        logger.debug("Attempting to find user by ID: {}", id);
        return userRepository.findById(id).map(this::convertToUserResponseDTO);
    }

    /**
     * Retrieves all users and converts them to UserResponseDTOs.
     *
     * @return A list of all UserResponseDTOs.
     */
    public List<UserResponseDTO> getAllUsers() {
        logger.debug("Retrieving all users.");
        return userRepository.findAll().stream()
                .map(this::convertToUserResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates a user's role. This should only be accessible by ADMIN users.
     *
     * @param userId The ID of the user to update.
     * @param newRole The new role to assign ("ADMIN" or "NORMAL_USER").
     * @return The updated UserResponseDTO.
     * @throws IllegalArgumentException if user not found or role is invalid.
     */
    @Transactional
    public UserResponseDTO updateUserRole(Long userId, String newRole) {
        logger.info("Attempting to update role for user ID: {} to role: {}", userId, newRole);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("User not found with ID: {}", userId);
                    return new IllegalArgumentException("User not found with ID: " + userId);
                });

        if (!"ADMIN".equals(newRole) && !"NORMAL_USER".equals(newRole)) {
            logger.warn("Invalid role specified: {}", newRole);
            throw new IllegalArgumentException("Invalid role: " + newRole + ". Role must be 'ADMIN' or 'NORMAL_USER'.");
        }

        user.setRole(newRole);
        User updatedUser = userRepository.save(user);
        logger.info("Successfully updated role for user ID: {} to: {}", userId, newRole);
        return convertToUserResponseDTO(updatedUser); // Convert to DTO before returning
    }

    /**
     * Converts a User entity to a UserResponseDTO.
     *
     * @param user The User entity to convert.
     * @return The corresponding UserResponseDTO.
     */
    private UserResponseDTO convertToUserResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }
}
