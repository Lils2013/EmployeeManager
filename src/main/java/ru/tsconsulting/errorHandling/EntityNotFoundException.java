package ru.tsconsulting.errorHandling;

public abstract class EntityNotFoundException extends RuntimeException{
    private String entityDetail;
    public EntityNotFoundException(String entityDetail) {
        this.entityDetail = entityDetail;
    }
    public String getEntityDetail() {
        return entityDetail;
    }
    public abstract String getMessage();
}
