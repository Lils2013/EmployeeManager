package ru.tsconsulting.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Department not specified.")
public class DepartmentNotSpecifiedException extends RuntimeException {

    public String getMessage() {
        return "Department not specified.";
    }
}
