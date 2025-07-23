package com.learning.core.exceptions;

public class GeneratorError extends RuntimeException {
    public GeneratorError() {
    }

    public GeneratorError(String message) {
        super(message);
    }
}
