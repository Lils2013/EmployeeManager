package ru.tsconsulting.error_handling.notification_exceptions;

public class NotUniqueUsernameException extends RuntimeException {

    String employeeDetails;
    public NotUniqueUsernameException(String employeeDetails) {
        this.employeeDetails = employeeDetails;
    }

    @Override
    public String getMessage() {
        return "Username [" + employeeDetails + "is not unique.";
    }
}
