package com.kaiquesouzapereira.javaavaliacao.modules.documents.Service;

import com.kaiquesouzapereira.javaavaliacao.modules.documents.Entity.DocumentEntity;
import com.kaiquesouzapereira.javaavaliacao.modules.documents.Repository.DocumentRepository;
import com.kaiquesouzapereira.javaavaliacao.modules.exceptions.DocumentNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ChangeStatusDocumentService {
    @Autowired
    private DocumentRepository documentRepository;
    public void changeStatus(String id, String status){
        DocumentEntity doc = documentRepository.findById(UUID.fromString(id)).orElseThrow(() -> new DocumentNotFound(id));
        doc.setStatus(DocumentEntity.fromValue(status));
        documentRepository.save(doc);
        return;
    }
}
