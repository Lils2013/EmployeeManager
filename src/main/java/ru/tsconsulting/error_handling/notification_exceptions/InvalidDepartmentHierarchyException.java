package ru.tsconsulting.error_handling.notification_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid hierarchy of departments.")
public class InvalidDepartmentHierarchyException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Invalid hierarchy of departments.";
    }
}
