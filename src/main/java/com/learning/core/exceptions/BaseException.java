package com.learning.core.exceptions;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final String userMessage;

    public BaseException(String message) {
        super(message);
        this.userMessage = message;
    }

    public BaseException(String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
    }
}
