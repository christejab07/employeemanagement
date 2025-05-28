package com.example.employeemanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for User registration and login requests.
 * Used to receive user input and apply validation rules before processing.
 */
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all fields
public class UserRequestDTO {

    @NotBlank(message = "Username cannot be empty") // Validation: field cannot be null or empty
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters") // Validation: size constraints
    private String username;

    @NotBlank(message = "Password cannot be empty") // Validation: field cannot be null or empty
    @Size(min = 6, message = "Password must be at least 6 characters long") // Validation: minimum size
    private String password;

    @NotBlank(message = "Email cannot be empty") // Validation: field cannot be null or empty
    @Email(message = "Email should be a valid email address") // Validation: valid email format
    private String email;
}
