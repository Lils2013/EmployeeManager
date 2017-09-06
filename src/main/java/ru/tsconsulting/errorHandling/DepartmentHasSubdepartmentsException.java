package ru.tsconsulting.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Department has subdepartments.")
public class DepartmentHasSubdepartmentsException extends RuntimeException  {
    private String departmentDetail;
    public DepartmentHasSubdepartmentsException(String departmentDetail) {
        this.departmentDetail = departmentDetail;
    }
    public String getDepartmentDetail() {
        return departmentDetail;
    }
    public String getMessage() {
        return "Department [" + getDepartmentDetail() + "] has subdepartments.";
    }
}
