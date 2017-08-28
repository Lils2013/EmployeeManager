package ru.tsconsulting.errorHandling;

public class EmployeeNotFoundException extends RuntimeException {

    private long employeeId;
    public EmployeeNotFoundException(long employeeId) {
        this.employeeId = employeeId;
    }
    public long getEmployeeId() {
        return employeeId;
    }
}
