package ru.tsconsulting.errorHandling;

public class DepartmentNotFoundException extends RuntimeException {

    private long departmentId;
    public DepartmentNotFoundException(long departmentId) {
        this.departmentId = departmentId;
    }
    public long getDepartmentId() {
        return departmentId;
    }
}
