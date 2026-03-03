package com.kaiquesouzapereira.javaavaliacao.modules.documents.Service;

import com.kaiquesouzapereira.javaavaliacao.modules.documents.Entity.DocumentEntity;
import com.kaiquesouzapereira.javaavaliacao.modules.documents.Repository.DocumentRepository;
import com.kaiquesouzapereira.javaavaliacao.modules.exceptions.DocumentNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Service
public class UpdateDocumentService {
    @Autowired
    private DocumentRepository documentRepository;
    public void  updateDocument(String id, MultipartFile file) throws IOException {
        DocumentEntity doc = documentRepository.findById(UUID.fromString(id)).orElseThrow(() -> new DocumentNotFound(id));

        byte[] novos = file.getBytes();
        byte[] antigos = doc.getData();

        if (!Arrays.equals(novos, antigos)) {
            doc.setData(novos);
            doc.setName(file.getOriginalFilename());
            doc.setStatus(DocumentEntity.Status.UPDATED);
            documentRepository.save(doc);
        }
    }
}
