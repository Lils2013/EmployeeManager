package ru.tsconsulting.error_handling.already_exist_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Grade already exists.")
public class EmployeeAlreadyExistException extends EntityAlreadyExistsException{

    public EmployeeAlreadyExistException(String entityDetail) {
        super(entityDetail);
    }

    @Override
    public String getMessage() {
        return "Employee [" + getEntityDetail() + "] already exists.";
    }
}