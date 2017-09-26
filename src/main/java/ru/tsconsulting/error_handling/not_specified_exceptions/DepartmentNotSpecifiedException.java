package ru.tsconsulting.error_handling.not_specified_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Department not specified.")
public class DepartmentNotSpecifiedException extends AttributeNotSpecifiedException {

    public String getMessage() {
        return "Department not specified.";
    }
}
