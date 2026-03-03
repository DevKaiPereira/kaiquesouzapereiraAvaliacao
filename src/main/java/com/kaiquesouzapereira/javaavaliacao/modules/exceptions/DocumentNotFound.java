package com.kaiquesouzapereira.javaavaliacao.modules.exceptions;

public class DocumentNotFound extends RuntimeException {
    public DocumentNotFound(String id) {
        super("Document " + id + " not Found");
    }

}
