package ru.tsconsulting.errorHandling.not_specified_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Not enough attributes")
public class NoAttributesProvidedException extends AttributeNotSpecifiedException {

    @Override
    public String getMessage() {
        return "Not enough attributes";
    }
}
