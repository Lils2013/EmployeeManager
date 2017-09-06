package ru.tsconsulting.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Invalid parameters")
public class InvalidParametersException extends RuntimeException{
    public InvalidParametersException() {
    }

    @Override
    public String getMessage() {
        return "Invalid Parameters";
    }
}
