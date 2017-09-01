package ru.tsconsulting.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Department not specified.")
public class DepartmentNotSpecifiedException extends RuntimeException {

    public DepartmentNotSpecifiedException(long departmentId) {
        this.departmentId = departmentId;
    }

    private long departmentId;

    public long getDepartmentId() {
        return departmentId;
    }

    public String getMessage() {
        return "Department [" + getDepartmentId() + "] not specified.";
    }
}
