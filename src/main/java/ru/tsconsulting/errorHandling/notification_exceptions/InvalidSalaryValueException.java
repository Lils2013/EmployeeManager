package ru.tsconsulting.errorHandling.notification_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid salary value.")
public class InvalidSalaryValueException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Invalid salary value.";
    }
}
