package com.example.employeemanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate; // For storing hire date

/**
 * Represents an employee within the organization.
 * This entity maps to the 'employees' table in the database.
 */
@Entity // Marks this class as a JPA entity
@Table(name = "employees") // Specifies the table name in the database
@Data // Lombok annotation to generate getters, setters, toString, equals, and hashCode methods
@NoArgsConstructor // Lombok annotation to generate a no-argument constructor
@AllArgsConstructor // Lombok annotation to generate a constructor with all fields
public class Employee {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configures the primary key to be auto-incremented by the database
    private Long id; // Unique identifier for the employee

    @Column(nullable = false) // Column property: cannot be null
    private String firstName; // Employee's first name

    @Column(nullable = false) // Column property: cannot be null
    private String lastName; // Employee's last name

    @Column(nullable = false, unique = true) // Column properties: cannot be null and must be unique
    private String email; // Employee's email address

    @Column(nullable = true) // Column property: can be null
    private String phoneNumber; // Employee's phone number

    @Column(nullable = false) // Column property: cannot be null
    private LocalDate hireDate; // Date the employee was hired

    @Column(nullable = false) // Column property: cannot be null
    private Double salary; // Employee's salary

    @Column(nullable = false) // Column property: cannot be null
    private String jobRole; // Employee's specific job role (e.g., "Manager", "Analyst", "Designer")

    // Many-to-One relationship with Department.
    // Many employees can belong to one department.
    // '@JoinColumn' specifies the foreign key column in the 'employees' table.
    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetching to avoid loading department unless explicitly needed
    @JoinColumn(name = "department_id", nullable = false) // Foreign key column name, cannot be null
    private Department department; // The department this employee belongs to
}
