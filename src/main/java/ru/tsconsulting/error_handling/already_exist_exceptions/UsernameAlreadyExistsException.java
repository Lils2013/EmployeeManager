package ru.tsconsulting.error_handling.already_exist_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Username already exists.")
public class UsernameAlreadyExistsException extends EntityAlreadyExistsException {
    public UsernameAlreadyExistsException(String employeeDetails) {
        super(employeeDetails);
    }

    @Override
    public String getMessage() {
        return "Username [" + getEntityDetail() + "] already exists.";
    }
}
