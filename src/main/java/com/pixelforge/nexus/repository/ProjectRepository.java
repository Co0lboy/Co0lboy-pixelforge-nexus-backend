package com.pixelforge.nexus.repository;

import com.pixelforge.nexus.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // We can add custom query methods here if needed in the future.
}



