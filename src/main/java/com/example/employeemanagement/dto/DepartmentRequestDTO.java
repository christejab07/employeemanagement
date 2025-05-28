package com.example.employeemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for Department creation and update requests.
 * Used to receive department input and apply validation rules.
 */
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all fields
public class DepartmentRequestDTO {

    private Long id; // Included for update operations, not required for creation

    @NotBlank(message = "Department name cannot be empty") // Validation: field cannot be null or empty
    @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters") // Validation: size constraints
    private String name; // Name of the department

    @Size(max = 255, message = "Location cannot exceed 255 characters") // Validation: max size
    private String location; // Location of the department (optional)
}
