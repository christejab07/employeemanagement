package com.example.employeemanagement.repository;

import com.example.employeemanagement.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for the Department entity.
 * Provides standard CRUD operations for Department objects.
 */
@Repository // Marks this interface as a Spring Data JPA repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * Finds a Department by its name.
     *
     * @param name The name of the department to search for.
     * @return An Optional containing the Department if found, or empty if not found.
     */
    Optional<Department> findByName(String name);

    /**
     * Checks if a Department with the given name already exists.
     *
     * @param name The name of the department to check.
     * @return true if a department with the name exists, false otherwise.
     */
    boolean existsByName(String name);
}
