package com.learning.core.exceptions;

public class CommandTemplateError extends RuntimeException {
    public CommandTemplateError() {
    }

    public CommandTemplateError(String message) {
        super(message);
    }
}
