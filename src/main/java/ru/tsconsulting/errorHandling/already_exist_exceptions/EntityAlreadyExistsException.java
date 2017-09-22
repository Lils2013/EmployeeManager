package ru.tsconsulting.errorHandling.already_exist_exceptions;

public abstract class EntityAlreadyExistsException extends RuntimeException{
    private String entityDetail;
    public EntityAlreadyExistsException(String entityDetail) {
        this.entityDetail = entityDetail;
    }
    public String getEntityDetail() {
        return entityDetail;
    }
    public abstract String getMessage();
}
