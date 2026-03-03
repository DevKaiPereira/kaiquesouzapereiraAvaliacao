package com.kaiquesouzapereira.javaavaliacao.modules.documents.Service;

import com.kaiquesouzapereira.javaavaliacao.modules.documents.Entity.DocumentEntity;
import com.kaiquesouzapereira.javaavaliacao.modules.documents.Repository.DocumentRepository;
import com.kaiquesouzapereira.javaavaliacao.modules.exceptions.DocumentNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteDocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public void delete(String id)  {
        DocumentEntity document = documentRepository.findById(UUID.fromString(id)).orElseThrow(() ->  new DocumentNotFound(id));

        if (document.getStatus() == DocumentEntity.Status.DELETED) {
            throw new DocumentNotFound(document.getId().toString());
        }

        document.setStatus(DocumentEntity.Status.DELETED);
        documentRepository.save(document);
    }
}