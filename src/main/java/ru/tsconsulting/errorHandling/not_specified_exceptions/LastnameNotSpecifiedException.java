package ru.tsconsulting.errorHandling.not_specified_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Last name not specified.")
public class LastnameNotSpecifiedException extends AttributeNotSpecifiedException {

    public String getMessage() {
        return "Last name not specified.";
    }
}

