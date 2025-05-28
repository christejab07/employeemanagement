package com.example.employeemanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for User responses.
 * Used to send user details to the client, excluding sensitive information like password hashes.
 */
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all fields
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String role; // User's role in the system (e.g., "ADMIN", "NORMAL_USER")
}
