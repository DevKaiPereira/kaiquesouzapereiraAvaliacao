package com.kaiquesouzapereira.javaavaliacao.modules.exceptions;

public class UserAlreadyExists extends RuntimeException{
    public UserAlreadyExists(String email) {
        super("User " + email + " with email already exists");
    }
}
