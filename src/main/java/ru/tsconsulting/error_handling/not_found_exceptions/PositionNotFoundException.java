package ru.tsconsulting.error_handling.not_found_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Position not found.")
public class PositionNotFoundException extends EntityNotFoundException {
    public PositionNotFoundException(String positionDetail) {
        super(positionDetail);
    }
    @Override
    public String getMessage() {
        return "Position [" + getEntityDetail() + "] not found.";
    }
}
