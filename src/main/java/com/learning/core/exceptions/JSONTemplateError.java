package com.learning.core.exceptions;

public class JSONTemplateError extends RuntimeException {
    public JSONTemplateError() {
    }

    public JSONTemplateError(String message) {
        super(message);
    }
}
