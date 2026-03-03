package com.kaiquesouzapereira.javaavaliacao.modules.users.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchConnectionDetails;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {
    @NotBlank(message = "Name is required.")
    private String username;
    @NotBlank(message = "Password is required.")
    @Length(min = 8, message = "The password must be at least 8 characters long.")
    private String password;
    @NotBlank(message = "Email is required.")
    @Email(message = "Email invalid")
    private String email;
    @NotBlank(message = "Position is required.")
    private String position;
}
