package com.pixelforge.nexus.controller;

import com.pixelforge.nexus.exception.ResourceNotFoundException;
import com.pixelforge.nexus.model.Project;
import com.pixelforge.nexus.model.User;
import com.pixelforge.nexus.repository.ProjectRepository;
import com.pixelforge.nexus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects/{projectId}/team")
public class TeamController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    // Project Leads can assign developers to their projects
    // Note: A more complex check would be needed to ensure the lead is assigned to this project.
    // For now, any Project Lead can assign to any project.
    @PostMapping("/assign/{username}")
    @PreAuthorize("hasRole('PROJECT_LEAD')")
    public ResponseEntity<?> assignUserToProject(@PathVariable Long projectId, @PathVariable String username) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        project.getAssignedUsers().add(user);
        projectRepository.save(project);

        return ResponseEntity.ok("User assigned successfully to project.");
    }
}