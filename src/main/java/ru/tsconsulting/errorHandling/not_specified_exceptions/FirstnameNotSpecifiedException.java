package ru.tsconsulting.errorHandling.not_specified_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "First name not specified.")
public class FirstnameNotSpecifiedException extends AttributeNotSpecifiedException {

    public String getMessage() {
        return "First name not specified.";
    }
}
