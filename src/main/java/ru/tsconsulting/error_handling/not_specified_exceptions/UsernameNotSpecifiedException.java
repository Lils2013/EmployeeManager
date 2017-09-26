package ru.tsconsulting.error_handling.not_specified_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Username not specified.")
public class UsernameNotSpecifiedException extends AttributeNotSpecifiedException {

    public String getMessage() {
        return "Username not specified.";
    }
}
