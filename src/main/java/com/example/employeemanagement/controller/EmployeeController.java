package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeRequestDTO; // Changed import
import com.example.employeemanagement.dto.EmployeeResponseDTO; // New import
import com.example.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing Employee resources.
 * All endpoints require authentication. Both 'ADMIN' and 'NORMAL_USER' roles
 * can perform CRUD operations on employees as per the revised requirements.
 */
@RestController // Marks this class as a REST controller
@RequestMapping("/api/employees") // Base path for all endpoints in this controller
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class); // Logger for this class

    private final EmployeeService employeeService; // Inject EmployeeService

    /**
     * Constructor for EmployeeController.
     * Spring automatically injects EmployeeService.
     *
     * @param employeeService The service for employee-related operations.
     */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Creates a new employee.
     * Accessible by both 'ADMIN' and 'NORMAL_USER' roles.
     *
     * @param employeeRequestDTO The EmployeeRequestDTO containing employee details.
     * @return A ResponseEntity with the created EmployeeResponseDTO and HTTP status 201 (Created).
     */
    @PostMapping // Maps POST requests to /api/employees
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL_USER')") // Accessible by ADMIN or NORMAL_USER
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO) { // Changed parameter and return type
        logger.info("User attempting to create new employee: {}", employeeRequestDTO.getEmail());
        try {
            EmployeeResponseDTO createdEmployee = employeeService.createEmployee(employeeRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to create employee: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
        } catch (Exception e) {
            logger.error("An unexpected error occurred while creating employee: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    /**
     * Retrieves all employees.
     * Accessible by both 'ADMIN' and 'NORMAL_USER' roles.
     *
     * @return A ResponseEntity with a list of EmployeeResponseDTO entities and HTTP status 200 (OK).
     */
    @GetMapping // Maps GET requests to /api/employees
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL_USER')") // Accessible by ADMIN or NORMAL_USER
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() { // Changed return type
        logger.info("User requesting all employees.");
        List<EmployeeResponseDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees); // 200 OK
    }

    /**
     * Retrieves an employee by their ID.
     * Accessible by both 'ADMIN' and 'NORMAL_USER' roles.
     *
     * @param id The ID of the employee to retrieve.
     * @return A ResponseEntity with the EmployeeResponseDTO if found, or 404 (Not Found).
     */
    @GetMapping("/{id}") // Maps GET requests to /api/employees/{id}
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL_USER')") // Accessible by ADMIN or NORMAL_USER
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id) { // Changed return type
        logger.info("User requesting employee with ID: {}", id);
        Optional<EmployeeResponseDTO> employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok) // If employee found, return 200 OK
                .orElseGet(() -> {
                    logger.warn("Employee with ID: {} not found.", id);
                    return ResponseEntity.notFound().build(); // If not found, return 404 Not Found
                });
    }

    /**
     * Retrieves all employees belonging to a specific department.
     * Accessible by both 'ADMIN' and 'NORMAL_USER' roles.
     *
     * @param departmentId The ID of the department.
     * @return A ResponseEntity with a list of EmployeeResponseDTO entities and HTTP status 200 (OK).
     */
    @GetMapping("/by-department/{departmentId}") // Maps GET requests to /api/employees/by-department/{departmentId}
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL_USER')") // Accessible by ADMIN or NORMAL_USER
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployeesByDepartment(@PathVariable Long departmentId) { // Changed return type
        logger.info("User requesting employees for department ID: {}", departmentId);
        try {
            List<EmployeeResponseDTO> employees = employeeService.getEmployeesByDepartment(departmentId);
            return ResponseEntity.ok(employees);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to retrieve employees for department ID {}: {}", departmentId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        } catch (Exception e) {
            logger.error("An unexpected error occurred while retrieving employees for department ID {}: {}", departmentId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    /**
     * Updates an existing employee.
     * Accessible by both 'ADMIN' and 'NORMAL_USER' roles.
     *
     * @param id The ID of the employee to update.
     * @param employeeRequestDTO The EmployeeRequestDTO with updated details.
     * @return A ResponseEntity with the updated EmployeeResponseDTO and HTTP status 200 (OK).
     */
    @PutMapping("/{id}") // Maps PUT requests to /api/employees/{id}
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL_USER')") // Accessible by ADMIN or NORMAL_USER
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDTO employeeRequestDTO) { // Changed parameter and return type
        logger.info("User attempting to update employee with ID: {}", id);
        try {
            EmployeeResponseDTO updatedEmployee = employeeService.updateEmployee(id, employeeRequestDTO);
            return ResponseEntity.ok(updatedEmployee); // 200 OK
        } catch (IllegalArgumentException e) {
            logger.error("Failed to update employee with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
        } catch (Exception e) {
            logger.error("An unexpected error occurred while updating employee with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    /**
     * Deletes an employee by their ID.
     * Accessible by both 'ADMIN' and 'NORMAL_USER' roles.
     *
     * @param id The ID of the employee to delete.
     * @return A ResponseEntity with no content and HTTP status 204 (No Content).
     */
    @DeleteMapping("/{id}") // Maps DELETE requests to /api/employees/{id}
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL_USER')") // Accessible by ADMIN or NORMAL_USER
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        logger.info("User attempting to delete employee with ID: {}", id);
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            logger.error("Failed to delete employee with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        } catch (Exception e) {
            logger.error("An unexpected error occurred while deleting employee with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }
}
