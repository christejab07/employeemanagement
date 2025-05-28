package com.example.employeemanagement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for Department responses.
 * Used to send department details to the client, avoiding circular references with employees.
 */
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all fields
public class DepartmentResponseDTO {
    private Long id;
    private String name;
    private String location;
}
