package com.kaiquesouzapereira.javaavaliacao.modules.documents.Service;

import com.kaiquesouzapereira.javaavaliacao.modules.documents.Entity.DocumentEntity;
import com.kaiquesouzapereira.javaavaliacao.modules.documents.Repository.DocumentRepository;
import com.kaiquesouzapereira.javaavaliacao.modules.exceptions.DocumentNotFound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteDocumentServiceTest {

    @InjectMocks
    private DeleteDocumentService deleteDocumentService;

    @Mock
    private DocumentRepository documentRepository;

    @Test
    @DisplayName("Should successfully mark document as DELETED")
    void should_delete_document_successfully() {

        var id = UUID.randomUUID();
        var document = DocumentEntity.builder()
                .id(id)
                .status(DocumentEntity.Status.PROCESSING)
                .build();

        when(documentRepository.findById(id)).thenReturn(Optional.of(document));

        deleteDocumentService.delete(id.toString());

        assertThat(document.getStatus()).isEqualTo(DocumentEntity.Status.DELETED);
        verify(documentRepository, times(1)).save(document);
    }

    @Test
    @DisplayName("Should throw DocumentNotFound when document ID does not exist")
    void should_throw_exception_when_id_not_found() {
        var id = UUID.randomUUID();
        when(documentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DocumentNotFound.class, () -> {
            deleteDocumentService.delete(id.toString());
        });

        verify(documentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw DocumentNotFound when document is already DELETED")
    void should_throw_exception_when_status_is_already_deleted() {

        var id = UUID.randomUUID();
        var document = DocumentEntity.builder()
                .id(id)
                .status(DocumentEntity.Status.DELETED)
                .build();

        when(documentRepository.findById(id)).thenReturn(Optional.of(document));

        assertThrows(DocumentNotFound.class, () -> {
            deleteDocumentService.delete(id.toString());
        });

        verify(documentRepository, never()).save(any());
    }
}