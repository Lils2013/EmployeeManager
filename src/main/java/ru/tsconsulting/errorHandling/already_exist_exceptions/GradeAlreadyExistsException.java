package ru.tsconsulting.errorHandling.already_exist_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Grade already exists.")
public class GradeAlreadyExistsException extends EntityAlreadyExistsException {
    public GradeAlreadyExistsException(String gradeDetails) {
        super(gradeDetails);
    }

    @Override
    public String getMessage() {
        return "Grade [" + getEntityDetail() + "] already exists.";
    }
}
