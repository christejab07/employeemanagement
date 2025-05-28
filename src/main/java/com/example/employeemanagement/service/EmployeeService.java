package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeRequestDTO; // Changed import
import com.example.employeemanagement.dto.EmployeeResponseDTO; // New import
import com.example.employeemanagement.model.Department;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing Employee-related business logic.
 * Handles CRUD operations for employees.
 */
@Service // Marks this class as a Spring Service component
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class); // Logger for this class

    private final EmployeeRepository employeeRepository; // Inject EmployeeRepository
    private final DepartmentRepository departmentRepository; // Inject DepartmentRepository to find associated department

    /**
     * Constructor for EmployeeService.
     * Spring automatically injects EmployeeRepository and DepartmentRepository.
     *
     * @param employeeRepository The repository for Employee entities.
     * @param departmentRepository The repository for Department entities.
     */
    public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    /**
     * Creates a new employee.
     * Ensures the associated department exists and that the employee's email is unique.
     *
     * @param employeeRequestDTO The EmployeeRequestDTO containing employee details.
     * @return The created EmployeeResponseDTO.
     * @throws IllegalArgumentException if the department does not exist or if the email already exists.
     */
    @Transactional // Ensures the entire method runs within a single database transaction
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO employeeRequestDTO) { // Changed parameter type
        logger.info("Attempting to create new employee with email: {}", employeeRequestDTO.getEmail());

        // Check if employee email already exists
        if (employeeRepository.existsByEmail(employeeRequestDTO.getEmail())) {
            logger.warn("Employee creation failed: Employee with email '{}' already exists.", employeeRequestDTO.getEmail());
            throw new IllegalArgumentException("Employee with email '" + employeeRequestDTO.getEmail() + "' already exists.");
        }

        // Find the associated department
        Department department = departmentRepository.findById(employeeRequestDTO.getDepartmentId())
                .orElseThrow(() -> {
                    logger.warn("Employee creation failed: Department not found with ID: {}", employeeRequestDTO.getDepartmentId());
                    return new IllegalArgumentException("Department not found with ID: " + employeeRequestDTO.getDepartmentId());
                });

        // Create a new Employee entity from DTO
        Employee employee = new Employee();
        employee.setFirstName(employeeRequestDTO.getFirstName());
        employee.setLastName(employeeRequestDTO.getLastName());
        employee.setEmail(employeeRequestDTO.getEmail());
        employee.setPhoneNumber(employeeRequestDTO.getPhoneNumber());
        employee.setHireDate(employeeRequestDTO.getHireDate());
        employee.setSalary(employeeRequestDTO.getSalary());
        employee.setJobRole(employeeRequestDTO.getJobRole());
        employee.setDepartment(department); // Set the retrieved Department entity

        Employee savedEmployee = employeeRepository.save(employee); // Save the employee
        logger.info("Successfully created employee with ID: {}", savedEmployee.getId());
        return convertToEmployeeResponseDTO(savedEmployee); // Convert to DTO before returning
    }

    /**
     * Retrieves all employees and converts them to EmployeeResponseDTOs.
     *
     * @return A list of all EmployeeResponseDTOs.
     */
    public List<EmployeeResponseDTO> getAllEmployees() {
        logger.debug("Retrieving all employees.");
        return employeeRepository.findAll().stream()
                .map(this::convertToEmployeeResponseDTO) // Convert each entity to DTO
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an employee by their ID and converts it to EmployeeResponseDTO.
     *
     * @param id The ID of the employee to retrieve.
     * @return An Optional containing the EmployeeResponseDTO if found.
     */
    public Optional<EmployeeResponseDTO> getEmployeeById(Long id) {
        logger.debug("Retrieving employee with ID: {}", id);
        return employeeRepository.findById(id)
                .map(this::convertToEmployeeResponseDTO); // Convert entity to DTO if found
    }

    /**
     * Updates an existing employee.
     * Ensures the employee and associated department exist, and handles email uniqueness if the email is changed.
     *
     * @param id The ID of the employee to update.
     * @param employeeRequestDTO The EmployeeRequestDTO with updated details.
     * @return The updated EmployeeResponseDTO.
     * @throws IllegalArgumentException if the employee or department does not exist, or if the new email conflicts.
     */
    @Transactional
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO employeeRequestDTO) { // Changed parameter type
        logger.info("Attempting to update employee with ID: {}", id);

        // Find the existing employee
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Employee update failed: Employee not found with ID: {}", id);
                    return new IllegalArgumentException("Employee not found with ID: " + id);
                });

        // Check for email uniqueness if the email is being changed
        if (!existingEmployee.getEmail().equals(employeeRequestDTO.getEmail()) && employeeRepository.existsByEmail(employeeRequestDTO.getEmail())) {
            logger.warn("Employee update failed: New email '{}' already exists for another employee.", employeeRequestDTO.getEmail());
            throw new IllegalArgumentException("Employee with email '" + employeeRequestDTO.getEmail() + "' already exists.");
        }

        // Find the associated department (in case it changed)
        Department department = departmentRepository.findById(employeeRequestDTO.getDepartmentId())
                .orElseThrow(() -> {
                    logger.warn("Employee update failed: Department not found with ID: {}", employeeRequestDTO.getDepartmentId());
                    return new IllegalArgumentException("Department not found with ID: " + employeeRequestDTO.getDepartmentId());
                });

        // Update fields
        existingEmployee.setFirstName(employeeRequestDTO.getFirstName());
        existingEmployee.setLastName(employeeRequestDTO.getLastName());
        existingEmployee.setEmail(employeeRequestDTO.getEmail());
        existingEmployee.setPhoneNumber(employeeRequestDTO.getPhoneNumber());
        existingEmployee.setHireDate(employeeRequestDTO.getHireDate());
        existingEmployee.setSalary(employeeRequestDTO.getSalary());
        existingEmployee.setJobRole(employeeRequestDTO.getJobRole());
        existingEmployee.setDepartment(department); // Update the department

        Employee updatedEmployee = employeeRepository.save(existingEmployee); // Save updated employee
        logger.info("Successfully updated employee with ID: {}", updatedEmployee.getId());
        return convertToEmployeeResponseDTO(updatedEmployee); // Convert to DTO before returning
    }

    /**
     * Deletes an employee by their ID.
     *
     * @param id The ID of the employee to delete.
     * @throws IllegalArgumentException if the employee does not exist.
     */
    @Transactional
    public void deleteEmployee(Long id) {
        logger.info("Attempting to delete employee with ID: {}", id);

        // Check if employee exists before deleting
        if (!employeeRepository.existsById(id)) {
            logger.warn("Employee deletion failed: Employee not found with ID: {}", id);
            throw new IllegalArgumentException("Employee not found with ID: " + id);
        }

        employeeRepository.deleteById(id); // Delete the employee
        logger.info("Successfully deleted employee with ID: {}", id);
    }

    /**
     * Retrieves all employees belonging to a specific department and converts them to EmployeeResponseDTOs.
     *
     * @param departmentId The ID of the department.
     * @return A list of employees in the specified department.
     * @throws IllegalArgumentException if the department does not exist.
     */
    public List<EmployeeResponseDTO> getEmployeesByDepartment(Long departmentId) {
        logger.debug("Retrieving employees for department ID: {}", departmentId);
        if (!departmentRepository.existsById(departmentId)) {
            logger.warn("Failed to retrieve employees: Department not found with ID: {}", departmentId);
            throw new IllegalArgumentException("Department not found with ID: " + departmentId);
        }
        return employeeRepository.findByDepartment_IdOrderByLastNameAscFirstNameAsc(departmentId).stream()
                .map(this::convertToEmployeeResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts an Employee entity to an EmployeeResponseDTO.
     *
     * @param employee The Employee entity to convert.
     * @return The corresponding EmployeeResponseDTO.
     */
    private EmployeeResponseDTO convertToEmployeeResponseDTO(Employee employee) {
        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getHireDate(),
                employee.getSalary(),
                employee.getJobRole(),
                employee.getDepartment().getId(), // Get only the department ID
                employee.getDepartment().getName() // Optionally get the department name
        );
    }
}
