package com.learning.core.exceptions;

public class SchemaError extends RuntimeException {
    public SchemaError() {
    }

    public SchemaError(String message) {
        super(message);
    }
}
