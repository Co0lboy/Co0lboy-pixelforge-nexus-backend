package com.pixelforge.nexus.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Lob // Specifies that the column should be treated as a Large Object
    private String description;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.ACTIVE;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Many-to-Many relationship with User
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_assignments", // The name of our junction table
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> assignedUsers = new HashSet<>();

    // One-to-Many relationship with ProjectDocument
    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.ALL, // Operations on Project will cascade to its documents
            orphanRemoval = true
    )
    private Set<ProjectDocument> documents = new HashSet<>();
}
