package ru.tsconsulting.errorHandling;

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
