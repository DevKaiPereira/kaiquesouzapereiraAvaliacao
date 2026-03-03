package com.kaiquesouzapereira.javaavaliacao.modules.exceptions;

public class InvalidCredentials extends RuntimeException {
    public InvalidCredentials() {
        super("Invalid Credentials!");
    }
}
