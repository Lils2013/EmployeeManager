package ru.tsconsulting.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Component
public class ExceptionHandlers {

    @ExceptionHandler(DepartmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestError departmentNotFound(DepartmentNotFoundException e) {
        long departmentId = e.getDepartmentId();
        return new RestError(1, "Department [" + departmentId + "] not found");
    }
}
