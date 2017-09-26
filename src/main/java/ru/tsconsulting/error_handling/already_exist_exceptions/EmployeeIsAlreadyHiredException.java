package ru.tsconsulting.error_handling.already_exist_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Employee is already hired.")
public class EmployeeIsAlreadyHiredException extends EntityAlreadyExistsException {
    public EmployeeIsAlreadyHiredException(String employeeDetail) {
        super(employeeDetail);
    }
    @Override
    public String getMessage() {
        return "Employee [" + getEntityDetail() + "] is already hired.";
    }
}
