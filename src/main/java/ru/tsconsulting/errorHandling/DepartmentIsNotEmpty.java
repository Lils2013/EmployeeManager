package ru.tsconsulting.errorHandling;

public class DepartmentIsNotEmpty extends RuntimeException {
    private long departmentId;

    public DepartmentIsNotEmpty(long departmentId) {
        this.departmentId = departmentId;
    }

    public long getDepartmentId() {
        return departmentId;
    }
}
