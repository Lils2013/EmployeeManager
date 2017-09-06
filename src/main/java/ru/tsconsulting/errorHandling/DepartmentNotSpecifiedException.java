package ru.tsconsulting.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Department not specified.")
public class DepartmentNotSpecifiedException extends RuntimeException {

    public DepartmentNotSpecifiedException(String departmentDetail) {
        this.departmentDetail = departmentDetail;
    }
    private String departmentDetail;
    public String getDepartmentDetail() {
        return departmentDetail;
    }
    public String getMessage() {
        return "Department [" + getDepartmentDetail() + "] not specified.";
    }
}
