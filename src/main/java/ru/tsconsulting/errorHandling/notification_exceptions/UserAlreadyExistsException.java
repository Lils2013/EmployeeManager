package ru.tsconsulting.errorHandling.notification_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.tsconsulting.errorHandling.not_found_exceptions.EntityNotFoundException;

@ResponseStatus(value = HttpStatus.ALREADY_REPORTED, reason = "User already exists.")
public class UserAlreadyExistsException extends EntityNotFoundException {
    public UserAlreadyExistsException(String userDetails) {
        super(userDetails);
    }

    @Override
    public String getMessage() {
        return "User [" + getEntityDetail() + "] already exists.";
    }
}
