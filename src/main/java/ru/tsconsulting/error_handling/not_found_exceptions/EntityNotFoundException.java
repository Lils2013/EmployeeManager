package ru.tsconsulting.error_handling.not_found_exceptions;

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
