package com.kaiquesouzapereira.javaavaliacao.modules.documents.Service;

import com.kaiquesouzapereira.javaavaliacao.modules.documents.Entity.DocumentEntity;
import com.kaiquesouzapereira.javaavaliacao.modules.documents.Repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UploadDocumentService {

    private final String DIRECTORY = System.getProperty("user.home") + "/IdeaProjects/javaavaliacao/src/public/upload/";
    @Autowired
    private DocumentRepository documentRepository;
    public String upload(MultipartFile file) throws IOException {
        DocumentEntity document = new DocumentEntity();
        document.setName(file.getOriginalFilename());
        document.setType(file.getContentType());
        document.setData(file.getBytes());
        File directory = new File(DIRECTORY);
        if (!directory.exists()) directory.mkdirs();
        Path path = Paths.get(DIRECTORY + file.getOriginalFilename());
        Files.write(path, document.getData());
        document.setPath(path.toString());
        documentRepository.save(document);

        return  document.getId().toString();
    }
}
