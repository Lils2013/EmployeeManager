package ru.tsconsulting.errorHandling;

public abstract class EntityNotFoundException extends RuntimeException{
    private long entityId;
    public EntityNotFoundException(long entityId) {
        this.entityId = entityId;
    }
    public long getEntityId() {
        return entityId;
    }
    public abstract String getMessage();
}
