package com.kaiquesouzapereira.javaavaliacao.modules.documents.Dto;


import com.kaiquesouzapereira.javaavaliacao.modules.documents.Entity.DocumentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponseDto {
    private UUID documentId;
    private String documentName;
    private String documentPath;
    private DocumentEntity.Status status;
    private byte[] data;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

