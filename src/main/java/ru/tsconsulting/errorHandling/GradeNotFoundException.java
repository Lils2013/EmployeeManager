package ru.tsconsulting.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Grade not found.")
public class GradeNotFoundException extends EntityNotFoundException {
    public GradeNotFoundException(long entityId) {
        super(entityId);
    }
    @Override
    public String getMessage() {
        return "Grade [" + getEntityId() + "] not found.";
    }
}
