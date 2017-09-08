package ru.tsconsulting.errorHandling.notification_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Department is not empty.")
public class DepartmentIsNotEmptyException extends RuntimeException {
    private String departmentDetail;
    public DepartmentIsNotEmptyException(String departmentDetail) {
        this.departmentDetail = departmentDetail;
    }
    public String getDepartmentDetail() {
        return departmentDetail;
    }
    public String getMessage() {
        return "Department [" + getDepartmentDetail() + "] is not empty.";
    }
}
