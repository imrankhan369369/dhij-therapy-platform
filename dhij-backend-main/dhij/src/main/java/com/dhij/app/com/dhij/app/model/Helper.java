package com.dhij.app.com.dhij.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "HELPER") // use upper-case to match SQL; H2 will treat unquoted as upper
public class Helper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Helper(Long id, String name, String role, String specialty) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.specialty = specialty;
    }

    public Helper() {}

    @NotBlank(message = "Name cannot be blank")
    @Column(name = "NAME")
    private String name;

    // Avoid reserved keyword "ROLE" in H2
    @NotBlank(message = "Role cannot be blank")
    @Column(name = "ROLE_NAME")
    private String role;

    @NotBlank(message = "speciality cannot be blank")
    @Column(name = "SPECIALTY")
    private String specialty;

    // getters/setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}
