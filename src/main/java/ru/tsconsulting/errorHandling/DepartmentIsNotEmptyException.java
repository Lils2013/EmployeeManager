package ru.tsconsulting.errorHandling;

public class DepartmentIsNotEmptyException extends RuntimeException {
    private long departmentId;
    public DepartmentIsNotEmptyException(long departmentId) {
        this.departmentId = departmentId;
    }
    public long getDepartmentId() {
        return departmentId;
    }
}
