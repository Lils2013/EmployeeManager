package ru.tsconsulting.errorHandling;

public class DepartmentHasSubdepartmentsException extends RuntimeException  {
    private long departmentId;
    public DepartmentHasSubdepartmentsException(long departmentId) {
        this.departmentId = departmentId;
    }
    public long getDepartmentId() {
        return departmentId;
    }
}
