package ru.tsconsulting.errorHandling;

public class DepartmentNotFoundException extends EntityNotFoundException {

    public DepartmentNotFoundException(long entityId) {
        super(entityId);
    }
    @Override
    public String getMessage() {
        return "Department [" + getEntityId() + "] not found.";
    }
}
