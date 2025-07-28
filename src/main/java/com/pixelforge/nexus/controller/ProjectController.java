package com.pixelforge.nexus.controller;

import com.pixelforge.nexus.dto.ProjectDto;
import com.pixelforge.nexus.exception.ResourceNotFoundException;
import com.pixelforge.nexus.model.Project;
import com.pixelforge.nexus.model.ProjectStatus;
import com.pixelforge.nexus.model.User;
import com.pixelforge.nexus.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    // All @PreAuthorize annotations have been removed. Security is now handled in SecurityConfig.
    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody ProjectDto projectDto) {
        Project project = new Project();
        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setDeadline(projectDto.getDeadline());
        Project savedProject = projectRepository.save(project);
        return ResponseEntity.ok(savedProject);
    }

    @GetMapping
    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        return ResponseEntity.ok(convertToDto(project));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> markProjectAsCompleted(@PathVariable Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        project.setStatus(ProjectStatus.COMPLETED);
        projectRepository.save(project);
        return ResponseEntity.ok("Project marked as completed.");
    }

    private ProjectDto convertToDto(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setName(project.getName());
        projectDto.setDescription(project.getDescription());
        projectDto.setDeadline(project.getDeadline());
        projectDto.setStatus(project.getStatus());
        projectDto.setAssignedUsernames(
                project.getAssignedUsers().stream()
                        .map(User::getUsername)
                        .collect(Collectors.toSet())
        );
        return projectDto;
    }
}
