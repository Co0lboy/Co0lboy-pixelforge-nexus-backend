package com.pixelforge.nexus.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DocumentDto {
    private Long id;
    private String fileName;
    private String fileType;
    private LocalDateTime uploadTimestamp;
}
