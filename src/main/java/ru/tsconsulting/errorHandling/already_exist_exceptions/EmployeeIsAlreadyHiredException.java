package ru.tsconsulting.errorHandling.already_exist_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.tsconsulting.errorHandling.not_found_exceptions.EntityNotFoundException;

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
