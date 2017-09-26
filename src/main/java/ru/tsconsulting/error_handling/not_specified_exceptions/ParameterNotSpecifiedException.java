package ru.tsconsulting.error_handling.not_specified_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Parameter is not specified.")
public class ParameterNotSpecifiedException extends AttributeNotSpecifiedException {

    public String getMessage() {
        return "Parameter is not specified.";
    }
}
