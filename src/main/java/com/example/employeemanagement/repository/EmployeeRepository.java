package com.example.employeemanagement.repository;

import com.example.employeemanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for the Employee entity.
 * Provides standard CRUD operations and allows for custom query methods.
 */
@Repository // Marks this interface as a Spring Data JPA repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Finds an Employee by their email address.
     *
     * @param email The email address of the employee to search for.
     * @return An Optional containing the Employee if found, or empty if not found.
     */
    Optional<Employee> findByEmail(String email);

    /**
     * Finds all Employees belonging to a specific Department, ordered by last name and then first name.
     *
     * @param departmentId The ID of the department.
     * @return A list of Employees in the specified department.
     */
    List<Employee> findByDepartment_IdOrderByLastNameAscFirstNameAsc(Long departmentId);

    /**
     * Checks if an Employee with the given email already exists.
     *
     * @param email The email to check.
     * @return true if an employee with the email exists, false otherwise.
     */
    boolean existsByEmail(String email);
}
