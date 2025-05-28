package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.UserRequestDTO; // Changed import
import com.example.employeemanagement.model.User;
import com.example.employeemanagement.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for authentication-related operations.
 * Handles user registration. Login is typically handled by Spring Security's filter chain.
 */
@RestController // Marks this class as a REST controller
@RequestMapping("/api/auth") // Base path for all endpoints in this controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class); // Logger for this class

    private final UserService userService; // Inject UserService

    /**
     * Constructor for AuthController.
     * Spring automatically injects UserService.
     *
     * @param userService The service for user-related operations.
     */
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     * This endpoint is publicly accessible (not secured by Spring Security by default).
     *
     * @param userRequestDTO The UserRequestDTO containing user registration details.
     * @return A ResponseEntity indicating success or failure.
     */
    @PostMapping("/register") // Maps POST requests to /api/auth/register
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO) { // Changed parameter type
        logger.info("Received registration request for username: {}", userRequestDTO.getUsername());
        try {
            User registeredUser = userService.registerUser(userRequestDTO);
            // Return a simplified response, not exposing the full User entity with hashed password
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully with ID: " + registeredUser.getId());
        } catch (IllegalArgumentException e) {
            logger.error("Registration failed for username {}: {}", userRequestDTO.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred during registration for username {}: {}", userRequestDTO.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred during registration.");
        }
    }
}
