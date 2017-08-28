package ru.tsconsulting.errorHandling;

public class GradeNotFoundException extends EntityNotFoundException {
    public GradeNotFoundException(long entityId) {
        super(entityId);
    }
    @Override
    public String getMessage() {
        return "Grade [" + getEntityId() + "] not found.";
    }
}
