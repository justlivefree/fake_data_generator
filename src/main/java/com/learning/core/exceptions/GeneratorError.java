package com.learning.core.exceptions;

public class GeneratorError extends BaseException {

    public GeneratorError(String message) {
        super(message);
    }

    public GeneratorError(String message, String userMessage) {
        super(message, userMessage);
    }
}
