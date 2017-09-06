package ru.tsconsulting.errorHandling;

public class InvalidDepartmentHierarchyException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Invalid hierarchy of departments.";
    }
}
