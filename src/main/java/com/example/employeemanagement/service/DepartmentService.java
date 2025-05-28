package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.DepartmentRequestDTO;
import com.example.employeemanagement.dto.DepartmentResponseDTO;
import com.example.employeemanagement.model.Department;
import com.example.employeemanagement.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing Department-related business logic.
 * Handles CRUD operations for departments.
 */
@Service // Marks this class as a Spring Service component
public class DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class); // Logger for this class

    private final DepartmentRepository departmentRepository; // Inject DepartmentRepository

    /**
     * Constructor for DepartmentService.
     * Spring automatically injects DepartmentRepository.
     *
     * @param departmentRepository The repository for Department entities.
     */
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    /**
     * Creates a new department.
     * Ensures that a department with the same name does not already exist.
     *
     * @param departmentRequestDTO The DepartmentRequestDTO containing department details.
     * @return The created DepartmentResponseDTO.
     * @throws IllegalArgumentException if a department with the same name already exists.
     */
    @Transactional // Ensures the entire method runs within a single database transaction
    public DepartmentResponseDTO createDepartment(DepartmentRequestDTO departmentRequestDTO) {
        logger.info("Attempting to create new department with name: {}", departmentRequestDTO.getName());

        // Check if department name already exists
        if (departmentRepository.existsByName(departmentRequestDTO.getName())) {
            logger.warn("Department creation failed: Department with name '{}' already exists.", departmentRequestDTO.getName());
            throw new IllegalArgumentException("Department with name '" + departmentRequestDTO.getName() + "' already exists.");
        }

        // Create a new Department entity from DTO
        Department department = new Department();
        department.setName(departmentRequestDTO.getName());
        department.setLocation(departmentRequestDTO.getLocation());

        Department savedDepartment = departmentRepository.save(department); // Save the department
        logger.info("Successfully created department with ID: {}", savedDepartment.getId());
        return convertToDepartmentResponseDTO(savedDepartment); // Convert to DTO before returning
    }

    /**
     * Retrieves all departments and converts them to DepartmentResponseDTOs.
     *
     * @return A list of all DepartmentResponseDTOs.
     */
    public List<DepartmentResponseDTO> getAllDepartments() {
        logger.debug("Retrieving all departments.");
        return departmentRepository.findAll().stream()
                .map(this::convertToDepartmentResponseDTO) // Convert each entity to DTO
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a department by its ID and converts it to DepartmentResponseDTO.
     *
     * @param id The ID of the department to retrieve.
     * @return An Optional containing the DepartmentResponseDTO if found.
     */
    public Optional<DepartmentResponseDTO> getDepartmentById(Long id) {
        logger.debug("Retrieving department with ID: {}", id);
        return departmentRepository.findById(id)
                .map(this::convertToDepartmentResponseDTO); // Convert entity to DTO if found
    }

    /**
     * Updates an existing department.
     * Ensures the department to be updated exists and handles name uniqueness if the name is changed.
     *
     * @param id The ID of the department to update.
     * @param departmentRequestDTO The DepartmentRequestDTO with updated details.
     * @return The updated DepartmentResponseDTO.
     * @throws IllegalArgumentException if the department does not exist or if the new name conflicts with an existing department.
     */
    @Transactional
    public DepartmentResponseDTO updateDepartment(Long id, DepartmentRequestDTO departmentRequestDTO) {
        logger.info("Attempting to update department with ID: {}", id);

        // Find the existing department
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Department update failed: Department not found with ID: {}", id);
                    return new IllegalArgumentException("Department not found with ID: " + id);
                });

        // Check for name uniqueness if the name is being changed
        if (!existingDepartment.getName().equals(departmentRequestDTO.getName()) && departmentRepository.existsByName(departmentRequestDTO.getName())) {
            logger.warn("Department update failed: New name '{}' already exists for another department.", departmentRequestDTO.getName());
            throw new IllegalArgumentException("Department with name '" + departmentRequestDTO.getName() + "' already exists.");
        }

        // Update fields
        existingDepartment.setName(departmentRequestDTO.getName());
        existingDepartment.setLocation(departmentRequestDTO.getLocation());

        Department updatedDepartment = departmentRepository.save(existingDepartment); // Save updated department
        logger.info("Successfully updated department with ID: {}", updatedDepartment.getId());
        return convertToDepartmentResponseDTO(updatedDepartment); // Convert to DTO before returning
    }

    /**
     * Deletes a department by its ID.
     *
     * @param id The ID of the department to delete.
     * @throws IllegalArgumentException if the department does not exist.
     */
    @Transactional
    public void deleteDepartment(Long id) {
        logger.info("Attempting to delete department with ID: {}", id);

        // Check if department exists before deleting
        if (!departmentRepository.existsById(id)) {
            logger.warn("Department deletion failed: Department not found with ID: {}", id);
            throw new IllegalArgumentException("Department not found with ID: " + id);
        }

        departmentRepository.deleteById(id); // Delete the department
        logger.info("Successfully deleted department with ID: {}", id);
    }

    /**
     * Converts a Department entity to a DepartmentResponseDTO.
     *
     * @param department The Department entity to convert.
     * @return The corresponding DepartmentResponseDTO.
     */
    private DepartmentResponseDTO convertToDepartmentResponseDTO(Department department) {
        return new DepartmentResponseDTO(department.getId(), department.getName(), department.getLocation());
    }
}
