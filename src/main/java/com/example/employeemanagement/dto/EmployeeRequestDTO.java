package com.example.employeemanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate; // For hire date

/**
 * Data Transfer Object for Employee creation and update requests.
 * Used to receive employee input and apply validation rules.
 */
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all fields
public class EmployeeRequestDTO {

    private Long id; // Included for update operations, not required for creation

    @NotBlank(message = "First name cannot be empty") // Validation: field cannot be null or empty
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters") // Validation: size constraints
    private String firstName;

    @NotBlank(message = "Last name cannot be empty") // Validation: field cannot be null or empty
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters") // Validation: size constraints
    private String lastName;

    @NotBlank(message = "Email cannot be empty") // Validation: field cannot be null or empty
    @Email(message = "Email should be a valid email address") // Validation: valid email format
    private String email;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters") // Validation: max size
    private String phoneNumber;

    @NotNull(message = "Hire date cannot be empty") // Validation: field cannot be null
    @PastOrPresent(message = "Hire date cannot be in the future") // Validation: date must be in the past or present
    private LocalDate hireDate;

    @NotNull(message = "Salary cannot be empty") // Validation: field cannot be null
    @Min(value = 0, message = "Salary must be a positive value") // Validation: minimum value
    private Double salary;

    @NotBlank(message = "Job role cannot be empty") // Validation: field cannot be null or empty
    @Size(min = 2, max = 50, message = "Job role must be between 2 and 50 characters") // Validation: size constraints
    private String jobRole;

    @NotNull(message = "Department ID cannot be empty") // Validation: field cannot be null
    private Long departmentId; // Foreign key to Department, represented as an ID in the DTO
}
