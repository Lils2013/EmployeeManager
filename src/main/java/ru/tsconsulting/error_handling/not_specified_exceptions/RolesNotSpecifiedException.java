package ru.tsconsulting.error_handling.not_specified_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Roles not specified.")
public class RolesNotSpecifiedException extends AttributeNotSpecifiedException {

    public String getMessage() {
        return "Roles not specified.";
    }
}
