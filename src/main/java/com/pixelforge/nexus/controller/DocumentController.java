package com.pixelforge.nexus.controller;

import com.pixelforge.nexus.exception.ResourceNotFoundException;
import com.pixelforge.nexus.model.Project;
import com.pixelforge.nexus.model.ProjectDocument;
import com.pixelforge.nexus.repository.ProjectDocumentRepository;
import com.pixelforge.nexus.repository.ProjectRepository;
import com.pixelforge.nexus.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/projects/{projectId}/documents")
public class DocumentController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectDocumentRepository projectDocumentRepository;

    // Admins and Project Leads can upload documents
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_LEAD')")
    public ResponseEntity<?> uploadDocument(@PathVariable Long projectId, @RequestParam("file") MultipartFile file) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        String filePath = fileStorageService.storeFile(file, projectId);

        ProjectDocument doc = new ProjectDocument();
        doc.setFileName(file.getOriginalFilename());
        doc.setFileType(file.getContentType());
        doc.setFilePath(filePath);
        doc.setProject(project);

        projectDocumentRepository.save(doc);

        return ResponseEntity.ok("File uploaded successfully.");
    }
}
