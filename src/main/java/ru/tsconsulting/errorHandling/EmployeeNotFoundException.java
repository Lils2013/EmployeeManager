package ru.tsconsulting.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Employee not found.")
public class EmployeeNotFoundException extends EntityNotFoundException {
    public EmployeeNotFoundException(long entityId) {
        super(entityId);
    }
    @Override
    public String getMessage() {
        return "Employee [" + getEntityId() + "] not found.";
    }
}
