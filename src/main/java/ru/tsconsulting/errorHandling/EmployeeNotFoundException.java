package ru.tsconsulting.errorHandling;

public class EmployeeNotFoundException extends EntityNotFoundException {
    public EmployeeNotFoundException(long entityId) {
        super(entityId);
    }
    @Override
    public String getMessage() {
        return "Employee [" + getEntityId() + "] not found.";
    }
}
