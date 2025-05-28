package com.example.employeemanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List; // For the one-to-many relationship with employees

/**
 * Represents a department within the organization.
 * This entity maps to the 'departments' table in the database.
 */
@Entity // Marks this class as a JPA entity
@Table(name = "departments") // Specifies the table name in the database
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all fields
public class Department {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configures the primary key to be auto-incremented by the database
    private Long id; // Unique identifier for the department

    @Column(nullable = false, unique = true) // Column properties: cannot be null and must be unique
    private String name; // Name of the department (e.g., "Human Resources", "Engineering")

    @Column(nullable = true) // Column property: can be null
    private String location; // Location of the department (e.g., "Building A", "Floor 3")
}
