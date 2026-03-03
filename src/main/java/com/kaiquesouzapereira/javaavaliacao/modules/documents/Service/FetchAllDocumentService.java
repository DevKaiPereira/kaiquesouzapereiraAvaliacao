package com.kaiquesouzapereira.javaavaliacao.modules.documents.Service;

import com.kaiquesouzapereira.javaavaliacao.modules.documents.Dto.DocumentResponseDto;
import com.kaiquesouzapereira.javaavaliacao.modules.documents.Entity.DocumentEntity;
import com.kaiquesouzapereira.javaavaliacao.modules.documents.Repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FetchAllDocumentService {

    @Autowired
    private DocumentRepository documentRepository;
    public List<DocumentResponseDto> fetchAllDocument(){
        var docs = documentRepository.findAll();
        return docs.stream()
                .filter(document -> document.getStatus() != DocumentEntity.Status.DELETED)
                .map(document -> DocumentResponseDto.builder()
                        .documentId(document.getId())
                        .documentPath(document.getPath())
                        .status(document.getStatus())
                        .documentName(document.getName())
                        .createdAt(document.getCreatedAt()).updatedAt(document.getUpdatedAt()).build()).toList();
    }
}
