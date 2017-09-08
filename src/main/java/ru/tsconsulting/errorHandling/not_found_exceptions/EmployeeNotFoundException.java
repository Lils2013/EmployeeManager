package ru.tsconsulting.errorHandling.not_found_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Employee not found.")
public class EmployeeNotFoundException extends EntityNotFoundException {
    public EmployeeNotFoundException(String employeeDetail) {
        super(employeeDetail);
    }
    @Override
    public String getMessage() {
        return "Employee [" + getEntityDetail() + "] not found.";
    }
}
