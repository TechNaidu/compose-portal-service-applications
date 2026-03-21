package com.portal.user.model;

/**
 * User - Model class representing a portal user entity.
 * <p>
 * Contains user profile information including name, email, role, department,
 * and account status. Used as the primary domain object throughout the User Service.
 * </p>
 *
 * @author Portal Team
 * @version 1.0.0-SNAPSHOT
 * @since 2026-03-21
 */
public class User {

    /** Unique identifier for the user. */
    private Long id;

    /** User's first name. */
    private String firstName;

    /** User's last name. */
    private String lastName;

    /** User's email address (unique). */
    private String email;

    /** User's role in the system (e.g., ADMIN, USER, MANAGER). */
    private String role;

    /** Department the user belongs to (e.g., Engineering, Marketing). */
    private String department;

    /** Account status: {@code "ACTIVE"} or {@code "INACTIVE"}. */
    private String status;

    /** Default no-arg constructor. */
    public User() {}

    /**
     * Constructs a fully initialized User instance.
     *
     * @param id         the unique user identifier
     * @param firstName  the user's first name
     * @param lastName   the user's last name
     * @param email      the user's email address
     * @param role       the user's role (ADMIN, USER, MANAGER)
     * @param department the department the user belongs to
     * @param status     the account status (ACTIVE, INACTIVE)
     */
    public User(Long id, String firstName, String lastName, String email, String role, String department, String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.department = department;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

