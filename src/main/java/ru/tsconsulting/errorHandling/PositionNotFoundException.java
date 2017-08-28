package ru.tsconsulting.errorHandling;

public class PositionNotFoundException extends EntityNotFoundException {
    public PositionNotFoundException(long entityId) {
        super(entityId);
    }
    @Override
    public String getMessage() {
        return "Position [" + getEntityId() + "] not found.";
    }
}
