package com.example.resultmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "parent_guardians")
public class ParentGuardian {
    @Id
    private String id = UUID.randomUUID().toString();
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private Relationship relationship;
    private String occupation;
    private Set<String> studentIds = new HashSet<>();
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum Relationship {
        FATHER, MOTHER, GUARDIAN, OTHER
    }
}
