package com.kaiquesouzapereira.javaavaliacao.modules.documents.Service;

import com.kaiquesouzapereira.javaavaliacao.modules.documents.Entity.DocumentEntity;
import com.kaiquesouzapereira.javaavaliacao.modules.documents.Repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UploadDocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private UploadDocumentService uploadDocumentService;

    private MockMultipartFile file;

    @BeforeEach
    void setUp() {

        file = new MockMultipartFile(
                "file",
                "documento_teste.pdf",
                "application/pdf",
                "conteudo do arquivo".getBytes()
        );
    }

    @Test
    @DisplayName("You should successfully save the file to disk and the data to the database.")
    void must_send_successfully() throws IOException {

        UUID idEsperado = UUID.randomUUID();
        DocumentEntity documentSalvo = new DocumentEntity();

        when(documentRepository.save(any(DocumentEntity.class))).thenAnswer(invocation -> {
            DocumentEntity entity = invocation.getArgument(0);

            java.lang.reflect.Field field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, idEsperado);
            return entity;
        });

        String resultadoId = uploadDocumentService.upload(file);
        assertEquals(idEsperado.toString(), resultadoId);

        Path pathEsperado = Paths.get(System.getProperty("user.home") + "/IdeaProjects/javaavaliacao/src/public/upload/documento_teste.pdf");
        assertTrue(Files.exists(pathEsperado), "The file should exist in the upload directory.");

        verify(documentRepository, times(1)).save(any(DocumentEntity.class));

        Files.deleteIfExists(pathEsperado);
    }
}