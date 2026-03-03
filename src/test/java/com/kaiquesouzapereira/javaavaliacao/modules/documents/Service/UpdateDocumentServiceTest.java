package com.kaiquesouzapereira.javaavaliacao.modules.documents.Service;

import com.kaiquesouzapereira.javaavaliacao.modules.documents.Entity.DocumentEntity;
import com.kaiquesouzapereira.javaavaliacao.modules.documents.Repository.DocumentRepository;
import com.kaiquesouzapereira.javaavaliacao.modules.exceptions.DocumentNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateDocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private UpdateDocumentService updateDocumentService;

    private UUID documentId;
    private DocumentEntity existingDoc;

    @BeforeEach
    void setUp() {
        documentId = UUID.randomUUID();
        existingDoc = new DocumentEntity();
        existingDoc.setData("conteudo_original".getBytes());
        existingDoc.setName("original.pdf");
    }

    @Test
    @DisplayName("You should update the document when the bytes sent are different from the current ones.")
    void must_update_document_successfully() throws IOException {

        MockMultipartFile newFile = new MockMultipartFile(
                "file", "novo_nome.pdf", "application/pdf", "novo_conteudo".getBytes()
        );
        when(documentRepository.findById(documentId)).thenReturn(Optional.of(existingDoc));

        updateDocumentService.updateDocument(documentId.toString(), newFile);

        assertEquals("novo_nome.pdf", existingDoc.getName());
        assertArrayEquals("novo_conteudo".getBytes(), existingDoc.getData());
        assertEquals(DocumentEntity.Status.UPDATED, existingDoc.getStatus());

        verify(documentRepository, times(1)).save(existingDoc);
    }

    @Test
    @DisplayName("You should not call the repository if the file content is identical.")
    void should_not_save_if_content_is_equal() throws IOException {

        byte[] conteudoIgual = "conteudo_original".getBytes();
        MockMultipartFile fileIgual = new MockMultipartFile(
                "file", "qualquer_nome.pdf", "application/pdf", conteudoIgual
        );
        when(documentRepository.findById(documentId)).thenReturn(Optional.of(existingDoc));

        updateDocumentService.updateDocument(documentId.toString(), fileIgual);

        verify(documentRepository, never()).save(any());
    }

    @Test
    @DisplayName("You should throw DocumentNotFound when the ID does not exist in the database.")
    void must_raise_a_document_not_found_exception() {

        String idString = documentId.toString();
        MockMultipartFile file = new MockMultipartFile("file", "teste.pdf", null, "bytes".getBytes());

        when(documentRepository.findById(documentId)).thenReturn(Optional.empty());

        DocumentNotFound exception = assertThrows(DocumentNotFound.class, () -> {
            updateDocumentService.updateDocument(idString, file);
        });

        assertTrue(exception.getMessage().contains(idString));
    }
}