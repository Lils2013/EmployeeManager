package ru.tsconsulting.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Department is not empty.")
public class DepartmentIsNotEmptyException extends RuntimeException {
    private long departmentId;
    public DepartmentIsNotEmptyException(long departmentId) {
        this.departmentId = departmentId;
    }
    public long getDepartmentId() {
        return departmentId;
    }
}
