package com.APIDevelopmentTask.Todo.app.application.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super("You do not have permission to access this Todo item.");
    }
}