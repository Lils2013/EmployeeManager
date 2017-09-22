package ru.tsconsulting.errorHandling.already_exist_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Position already exists.")
public class PositionAlreadyExistsException extends EntityAlreadyExistsException {
    public PositionAlreadyExistsException(String positionDetails) {
        super(positionDetails);
    }

    @Override
    public String getMessage() {
        return "Position [" + getEntityDetail() + "] already exists.";
    }
}
