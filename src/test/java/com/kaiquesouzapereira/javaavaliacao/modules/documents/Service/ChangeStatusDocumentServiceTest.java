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
class ChangeStatusDocumentServiceTest {

    @InjectMocks
    private ChangeStatusDocumentService changeStatusDocumentService;

    @Mock
    private DocumentRepository documentRepository;

    @Test
    @DisplayName("Should change document status successfully")
    void should_change_status_successfully() {
        var id = UUID.randomUUID();
        var document = DocumentEntity.builder()
                .id(id)
                .status(DocumentEntity.Status.PROCESSING)
                .build();

        when(documentRepository.findById(id)).thenReturn(Optional.of(document));

        changeStatusDocumentService.changeStatus(id.toString(), "DELETED");

        assertThat(document.getStatus()).isEqualTo(DocumentEntity.Status.DELETED);
        verify(documentRepository, times(1)).save(document);
    }

    @Test
    @DisplayName("Should throw DocumentNotFound when changing status of non-existent document")
    void should_throw_exception_when_document_not_found() {

        var id = UUID.randomUUID();
        when(documentRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(DocumentNotFound.class, () -> {
            changeStatusDocumentService.changeStatus(id.toString(), "DELETED");
        });
    }
}