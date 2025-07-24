package com.learning.core.exceptions;

public class CommandTemplateError extends BaseException {

    public CommandTemplateError(String message) {
        super(message);
    }

    public CommandTemplateError(String message, String userMessage) {
        super(message, userMessage);
    }
}
