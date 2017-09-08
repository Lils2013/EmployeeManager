package ru.tsconsulting.errorHandling.not_specified_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Certificate not specified.")
public class EmployeeNotSpecifiedException extends AttributeNotSpecifiedException {

    public String getMessage() {
        return "Employee not specified.";
    }
}