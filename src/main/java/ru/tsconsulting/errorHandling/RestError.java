package ru.tsconsulting.errorHandling;

public class RestError {
    private Errors errors;
    private String message;
    public RestError(Errors errors, String message) {
        this.errors = errors;
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public Errors getErrors() {
        return errors;
    }
}
