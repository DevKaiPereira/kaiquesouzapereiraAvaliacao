package com.kaiquesouzapereira.javaavaliacao.modules.users.Dto;

import lombok.Data;

@Data
public class AuthUserDto {
    private String email;
    private String password;

    public AuthUserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
