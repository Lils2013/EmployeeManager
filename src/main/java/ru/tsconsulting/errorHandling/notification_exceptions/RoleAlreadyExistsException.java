package ru.tsconsulting.errorHandling.notification_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.tsconsulting.errorHandling.not_found_exceptions.EntityNotFoundException;

@ResponseStatus(value = HttpStatus.ALREADY_REPORTED, reason = "Role already exists.")
public class RoleAlreadyExistsException extends EntityNotFoundException {
    public RoleAlreadyExistsException(String roleDetails) {
        super(roleDetails);
    }

    @Override
    public String getMessage() {
        return "Role [" + getEntityDetail() + "] already exists.";
    }
}
