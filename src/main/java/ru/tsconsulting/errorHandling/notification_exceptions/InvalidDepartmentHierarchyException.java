package ru.tsconsulting.errorHandling.notification_exceptions;

public class InvalidDepartmentHierarchyException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Invalid hierarchy of departments.";
    }
}
