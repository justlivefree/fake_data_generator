package com.learning.core.exceptions;

public class SchemaError extends BaseException {

    public SchemaError(String message) {
        super(message);
    }

    public SchemaError(String message, String userMessage) {
        super(message, userMessage);
    }
}
