package com.learning.core.exceptions;

public class InvalidTemplateFile extends RuntimeException {
    public InvalidTemplateFile(Throwable throwable) {
        super(throwable);
    }
}
