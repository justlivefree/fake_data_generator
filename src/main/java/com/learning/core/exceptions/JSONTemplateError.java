package com.learning.core.exceptions;

public class JSONTemplateError extends BaseException {

    public JSONTemplateError(String message) {
        super(message);
    }

    public JSONTemplateError(String message, String userMessage) {
        super(message, userMessage);
    }
}
