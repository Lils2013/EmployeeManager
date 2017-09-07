package ru.tsconsulting.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Employee is already fired.")
public class EmployeeIsAlreadyFiredException extends EntityNotFoundException {
    public EmployeeIsAlreadyFiredException(String employeeDetail) {
        super(employeeDetail);
    }
    @Override
    public String getMessage() {
        return "Employee [" + getEntityDetail() + "] if already hired.";
    }
}
