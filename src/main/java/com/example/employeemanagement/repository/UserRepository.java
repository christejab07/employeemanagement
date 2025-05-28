package com.example.employeemanagement.repository;

import com.example.employeemanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for the User entity.
 * Provides standard CRUD operations and allows for custom query methods.
 * Extends JpaRepository, specifying the entity type (User) and its primary key type (Long).
 */
@Repository // Marks this interface as a Spring Data JPA repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a User by their username.
     * Spring Data JPA automatically generates the query based on the method name.
     *
     * @param username The username to search for.
     * @return An Optional containing the User if found, or empty if not found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a User with the given username already exists.
     *
     * @param username The username to check.
     * @return true if a user with the username exists, false otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a User with the given email already exists.
     *
     * @param email The email to check.
     * @return true if a user with the email exists, false otherwise.
     */
    boolean existsByEmail(String email);
}
