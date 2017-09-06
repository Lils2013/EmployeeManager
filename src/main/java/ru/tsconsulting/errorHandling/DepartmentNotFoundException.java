package ru.tsconsulting.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Department not found.")
public class DepartmentNotFoundException extends EntityNotFoundException {

    public DepartmentNotFoundException(String departmentDetail) {
        super(departmentDetail);
    }
    @Override
    public String getMessage() {
        return "Department [" + getEntityDetail() + "] not found.";
    }
}
