package com.pixelforge.nexus.dto;

import com.pixelforge.nexus.model.ProjectStatus;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class ProjectDto {
    private Long id;
    private String name;
    private String description;
    private LocalDate deadline;
    private ProjectStatus status;
    private Set<String> assignedUsernames; // To show usernames in the response
}