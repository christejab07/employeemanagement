package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.DepartmentRequestDTO; // Changed import
import com.example.employeemanagement.dto.DepartmentResponseDTO; // New import
import com.example.employeemanagement.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation; // New import
import io.swagger.v3.oas.annotations.security.SecurityRequirement; // New import
import io.swagger.v3.oas.annotations.tags.Tag; // New import
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
 * REST Controller for managing Department resources.
 * All endpoints require authentication. Specific roles are required for certain operations.
 */
@RestController // Marks this class as a REST controller
@RequestMapping("/api/departments") // Base path for all endpoints in this controller
@Tag(name = "Department Management", description = "Operations related to departments") // New: Tag for grouping in Swagger UI
@SecurityRequirement(name = "basicAuth") // New: Applies basicAuth security to all methods in this controller by default
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class); // Logger for this class

    private final DepartmentService departmentService; // Inject DepartmentService

    /**
     * Constructor for DepartmentController.
     * Spring automatically injects DepartmentService.
     *
     * @param departmentService The service for department-related operations.
     */
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * Creates a new department.
     * Only users with 'ADMIN' role can access this endpoint.
     *
     * @param departmentRequestDTO The DepartmentRequestDTO containing department details.
     * @return A ResponseEntity with the created DepartmentResponseDTO and HTTP status 201 (Created).
     */
    @PostMapping // Maps POST requests to /api/departments
    @PreAuthorize("hasRole('ADMIN')") // Requires ADMIN role
    @Operation(summary = "Create a new department", description = "Only ADMIN users can create departments.") // New: Operation summary
    public ResponseEntity<DepartmentResponseDTO> createDepartment(@Valid @RequestBody DepartmentRequestDTO departmentRequestDTO) { // Changed parameter and return type
        logger.info("Admin user attempting to create new department: {}", departmentRequestDTO.getName());
        try {
            DepartmentResponseDTO createdDepartment = departmentService.createDepartment(departmentRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to create department: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
        } catch (Exception e) {
            logger.error("An unexpected error occurred while creating department: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    /**
     * Retrieves all departments.
     * Accessible by any authenticated user (ADMIN or NORMAL_USER).
     *
     * @return A ResponseEntity with a list of DepartmentResponseDTO entities and HTTP status 200 (OK).
     */
    @GetMapping // Maps GET requests to /api/departments
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL_USER')") // Accessible by ADMIN or NORMAL_USER
    @Operation(summary = "Get all departments", description = "Accessible by ADMIN and NORMAL_USER to retrieve all department records.") // New: Operation summary
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() { // Changed return type
        logger.info("User requesting all departments.");
        List<DepartmentResponseDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments); // 200 OK
    }

    /**
     * Retrieves a department by its ID.
     * Accessible by any authenticated user (ADMIN or NORMAL_USER).
     *
     * @param id The ID of the department to retrieve.
     * @return A ResponseEntity with the DepartmentResponseDTO if found, or 404 (Not Found).
     */
    @GetMapping("/{id}") // Maps GET requests to /api/departments/{id}
    @PreAuthorize("hasAnyRole('ADMIN', 'NORMAL_USER')") // Accessible by ADMIN or NORMAL_USER
    @Operation(summary = "Get department by ID", description = "Accessible by ADMIN and NORMAL_USER to retrieve a specific department by its ID.")
    public ResponseEntity<DepartmentResponseDTO> getDepartmentById(@PathVariable Long id) { // Changed return type
        logger.info("User requesting department with ID: {}", id);
        Optional<DepartmentResponseDTO> department = departmentService.getDepartmentById(id);
        return department.map(ResponseEntity::ok) // If department found, return 200 OK
                .orElseGet(() -> {
                    logger.warn("Department with ID: {} not found.", id);
                    return ResponseEntity.notFound().build(); // If not found, return 404 Not Found
                });
    }

    /**
     * Updates an existing department.
     * Only users with 'ADMIN' role can access this endpoint.
     *
     * @param id The ID of the department to update.
     * @param departmentRequestDTO The DepartmentRequestDTO with updated details.
     * @return A ResponseEntity with the updated DepartmentResponseDTO and HTTP status 200 (OK).
     */
    @PutMapping("/{id}") // Maps PUT requests to /api/departments/{id}
    @PreAuthorize("hasRole('ADMIN')") // Requires ADMIN role
    @Operation(summary = "Update a department", description = "Only ADMIN users can update department records.")
    public ResponseEntity<DepartmentResponseDTO> updateDepartment(@PathVariable Long id, @Valid @RequestBody DepartmentRequestDTO departmentRequestDTO) { // Changed parameter and return type
        logger.info("Admin user attempting to update department with ID: {}", id);
        try {
            DepartmentResponseDTO updatedDepartment = departmentService.updateDepartment(id, departmentRequestDTO);
            return ResponseEntity.ok(updatedDepartment); // 200 OK
        } catch (IllegalArgumentException e) {
            logger.error("Failed to update department with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 Bad Request
        } catch (Exception e) {
            logger.error("An unexpected error occurred while updating department with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    /**
     * Deletes a department by its ID.
     * Only users with 'ADMIN' role can access this endpoint.
     *
     * @param id The ID of the department to delete.
     * @return A ResponseEntity with no content and HTTP status 204 (No Content).
     */
    @DeleteMapping("/{id}") // Maps DELETE requests to /api/departments/{id}
    @PreAuthorize("hasRole('ADMIN')") // Requires ADMIN role
    @Operation(summary = "Delete a department", description = "Only ADMIN users can delete department records.")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        logger.info("Admin user attempting to delete department with ID: {}", id);
        try {
            departmentService.deleteDepartment(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            logger.error("Failed to delete department with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        } catch (Exception e) {
            logger.error("An unexpected error occurred while deleting department with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }
}
