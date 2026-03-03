package com.kaiquesouzapereira.javaavaliacao.modules.documents.Repository;

import com.kaiquesouzapereira.javaavaliacao.modules.documents.Entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {
    @Query(value = "SELECT COUNT(*) FROM documents", nativeQuery = true)
    long countTotalDocuments();

    @Query(value = "SELECT COALESCE(SUM(LENGTH(data)), 0) FROM documents", nativeQuery = true)
    long sumTotalBytes();
}
