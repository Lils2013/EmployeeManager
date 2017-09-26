package ru.tsconsulting.error_handling.already_exist_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Department already exists.")
public class DepartmentAlreadyExistsException extends EntityAlreadyExistsException {
    public DepartmentAlreadyExistsException(String departmentDetails) {
        super(departmentDetails);
    }

    @Override
    public String getMessage() {
        return "Department [" + getEntityDetail() + "] already exists.";
    }
}
