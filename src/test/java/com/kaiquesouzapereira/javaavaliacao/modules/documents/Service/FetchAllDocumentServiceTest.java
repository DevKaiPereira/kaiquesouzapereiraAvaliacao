package com.kaiquesouzapereira.javaavaliacao.modules.documents.Service;

import com.kaiquesouzapereira.javaavaliacao.modules.documents.Dto.DocumentResponseDto;
import com.kaiquesouzapereira.javaavaliacao.modules.documents.Entity.DocumentEntity;
import com.kaiquesouzapereira.javaavaliacao.modules.documents.Repository.DocumentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchAllDocumentServiceTest {

    @InjectMocks
    private FetchAllDocumentService fetchAllDocumentService;

    @Mock
    private DocumentRepository documentRepository;

    @Test
    @DisplayName("Should return only documents that are not DELETED")
    void should_return_filtered_documents() {
        // GIVEN
        var docActive = DocumentEntity.builder()
                .id(UUID.randomUUID())
                .name("Relatorio.pdf")
                .status(DocumentEntity.Status.PROCESSING)
                .build();

        var docDeleted = DocumentEntity.builder()
                .id(UUID.randomUUID())
                .name("Antigo.pdf")
                .status(DocumentEntity.Status.DELETED)
                .build();

        when(documentRepository.findAll()).thenReturn(List.of(docActive, docDeleted));

        List<DocumentResponseDto> result = fetchAllDocumentService.fetchAllDocument();

        assertThat(result).hasSize(1);

        assertThat(result.get(0).getDocumentName()).isEqualTo("Relatorio.pdf");
        assertThat(result.get(0).getStatus()).isEqualTo(DocumentEntity.Status.PROCESSING);
    }

    @Test
    @DisplayName("Should return empty list when all documents are DELETED")
    void should_return_empty_list_when_all_deleted() {

        var docDeleted = DocumentEntity.builder()
                .id(UUID.randomUUID())
                .status(DocumentEntity.Status.DELETED)
                .build();

        when(documentRepository.findAll()).thenReturn(List.of(docDeleted));

        List<DocumentResponseDto> result = fetchAllDocumentService.fetchAllDocument();

        assertThat(result).isEmpty();
    }
}