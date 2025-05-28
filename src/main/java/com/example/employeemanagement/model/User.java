package com.example.employeemanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents a system user who can log into the application.
 * This entity maps to the 'users' table in the database.
 * It contains credentials and the user's role for authorization.
 */
@Entity // Marks this class as a JPA entity
@Table(name = "users") // Specifies the table name in the database
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all fields
public class User {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configures the primary key to be auto-incremented by the database
    private Long id; // Unique identifier for the user

    @Column(nullable = false, unique = true) // Column properties: cannot be null and must be unique
    private String username; // User's login username

    @Column(nullable = false) // Column property: cannot be null
    private String password; // Hashed password for security (will be stored as hash, not plain text)

    @Column(nullable = false, unique = true) // Column properties: cannot be null and must be unique
    private String email; // User's email address

    @Column(nullable = false) // Column property: cannot be null
    private String role; // User's role in the system (e.g., "ADMIN", "NORMAL_USER")
}
