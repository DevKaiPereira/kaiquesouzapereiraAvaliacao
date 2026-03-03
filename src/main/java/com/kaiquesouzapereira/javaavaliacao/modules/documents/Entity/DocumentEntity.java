package com.kaiquesouzapereira.javaavaliacao.modules.documents.Entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name ="documents")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(length = 36)
    private UUID id;

    private String name;
    private String type;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;
    private String path;
    @Enumerated(EnumType.STRING)
    private Status status = Status.PROCESSING;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum Status {
       PROCESSING, READY, UPDATED, DELETED

        }
    @JsonCreator
    public static Status fromValue(String value) {
        if (value == null) {
            return null;
        }

        try {
            return Status.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Invalid Value");
        }
    }
}
