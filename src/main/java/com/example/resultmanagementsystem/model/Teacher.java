package com.example.resultmanagementsystem.model;

import com.example.resultmanagementsystem.model.Role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "teachers")
public class Teacher {

    @Id
    private String id = UUID.randomUUID().toString();
    private String firstname;
    private String lastname;
    private String email;

    @JsonIgnore
    private String password;

    private Set<String> departmentId;
    private Role role;
    private boolean isVerified = false;

    // Getter methods for commonly used properties
    public String getTeacherId() {
        return this.id;
    }

    public String getFullName() {
        return this.firstname + " " + this.lastname;
    }

    public String getUsername() {
        return email; // Use email for login
    }

    public boolean isEnabled() {
        return isVerified; // Ensure verification is required
    }

    // Helper method to check if teacher belongs to a specific department
    public boolean belongsToDepartment(String deptId) {
        return departmentId != null && departmentId.contains(deptId);
    }

    // Helper method to add a department
    public void addDepartment(String deptId) {
        if (departmentId != null) {
            departmentId.add(deptId);
        }
    }

    // Helper method to remove a department
    public void removeDepartment(String deptId) {
        if (departmentId != null) {
            departmentId.remove(deptId);
        }
    }
}