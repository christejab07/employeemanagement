package com.example.employeemanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for Employee responses.
 * Used to send employee details to the client, referencing department by ID to avoid circularity.
 */
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all fields
public class EmployeeResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate hireDate;
    private Double salary;
    private String jobRole;
    private Long departmentId; // Only include the department ID, not the full Department object
    private String departmentName; // Optionally include the department name for convenience
}
