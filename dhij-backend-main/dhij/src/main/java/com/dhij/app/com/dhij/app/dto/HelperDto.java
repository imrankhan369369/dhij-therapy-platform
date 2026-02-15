package com.dhij.app.com.dhij.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class HelperDto {
	
	
	

private long id;
    @NotBlank(message = "name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be 2-50 characters")
    private String name;

    @NotBlank(message = "Role is mandatory")
    private String role;

    @NotBlank(message = "specialty is mandatory")
    private String specialty;

    // getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }


	

}
