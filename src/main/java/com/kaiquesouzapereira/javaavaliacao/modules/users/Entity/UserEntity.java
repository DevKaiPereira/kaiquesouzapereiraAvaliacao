package com.kaiquesouzapereira.javaavaliacao.modules.users.Entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String username;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Position position;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum Position {
        ADMIN, MANAGER, USER
    }

    @JsonCreator
    public static Position fromValue(String value) {
        if (value == null) {
            return null;
        }

        try {
            return Position.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(
                    "Invalid Value");
        }
    }
}
