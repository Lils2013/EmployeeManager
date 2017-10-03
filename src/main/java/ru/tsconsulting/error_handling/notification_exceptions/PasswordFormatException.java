package ru.tsconsulting.error_handling.notification_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Password has incorrect format.")
public class PasswordFormatException extends RuntimeException  {
    private String password;
    public PasswordFormatException(String password) {
        this.password = password;
    }
    public String getMessage() {
        return "Password [" + password + "] has incorrect format. Must be alphanumeric and have length between 1 and 32";

    }
}
