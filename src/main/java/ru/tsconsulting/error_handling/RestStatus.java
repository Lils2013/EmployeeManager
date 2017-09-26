package ru.tsconsulting.error_handling;

public class RestStatus {
    private Status status;
    private String message;
    public RestStatus(Status status, String message) {
        this.status = status;
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public Status getStatus() {
        return status;
    }
}
