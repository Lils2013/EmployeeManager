package ru.tsconsulting.error_handling.notification_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Username has incorrect length.")
public class UsernameLengthException extends RuntimeException  {
    private String username;
    public UsernameLengthException(String username) {
        this.username = username;
    }
    public String getDepartmentDetail() {
        return username;
    }
    public String getMessage() {
        return "Username  has incorrect length.";
    }
}
