package ru.tsconsulting.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Department has subdepartments.")
public class DepartmentHasSubdepartmentsException extends RuntimeException  {
    private long departmentId;
    public DepartmentHasSubdepartmentsException(long departmentId) {
        this.departmentId = departmentId;
    }
    public long getDepartmentId() {
        return departmentId;
    }
}
