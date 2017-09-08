package ru.tsconsulting.errorHandling.not_found_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Grade not found.")
public class GradeNotFoundException extends EntityNotFoundException {
    public GradeNotFoundException(String gradeDetail) {
        super(gradeDetail);
    }
    @Override
    public String getMessage() {
        return "Grade [" + getEntityDetail() + "] not found.";
    }
}
