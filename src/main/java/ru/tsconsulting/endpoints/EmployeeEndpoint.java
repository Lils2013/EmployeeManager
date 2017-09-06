package ru.tsconsulting.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.tsconsulting.employee_ws.*;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.errorHandling.DepartmentNotFoundException;
import ru.tsconsulting.errorHandling.EmployeeNotFoundException;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.EmployeeRepository;

@Endpoint
public class EmployeeEndpoint {

    private static final String NAMESPACE_URI = "http://tsconsulting.ru/employee-ws";
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeeEndpoint(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "transferRequest")
    @ResponsePayload
    public TransferResponse transfer(@RequestPayload TransferRequest transferRequest) {
        Employee employee = employeeRepository.findByIdAndIsFiredIsFalse(transferRequest.getEmployeeId());
        if (employee == null) {
            throw new EmployeeNotFoundException(((Long)transferRequest.getEmployeeId()).toString());
        }
        Department newDepartment = departmentRepository.
                findByIdAndIsDismissedIsFalse(transferRequest.getNewDepartmentId());
        if (newDepartment == null) {
            throw new DepartmentNotFoundException(((Long)transferRequest.getNewDepartmentId()).toString());
        }
        employee.setDepartment(newDepartment);
        employeeRepository.save(employee);
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setEmployee(parseEmployee(employee));
        return transferResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "firingRequest")
    @ResponsePayload
    public FiringResponse fire(@RequestPayload FiringRequest firingRequest) {
        Employee employee = employeeRepository.findByIdAndIsFiredIsFalse(firingRequest.getEmployeeId());
        if (employee == null) {
            throw new EmployeeNotFoundException(((Long)firingRequest.getEmployeeId()).toString());
        }
        employee.setFired(true);
        employeeRepository.save(employee);
        return new FiringResponse();
    }

    private EmployeeForSOAP parseEmployee(Employee employee) {
        EmployeeForSOAP employeeForSOAP = new EmployeeForSOAP();
        employeeForSOAP.setFirstname(employee.getFirstname());
        employeeForSOAP.setLastname(employee.getLastname());
        employeeForSOAP.setMiddlename(employee.getMiddlename());
        employeeForSOAP.setBirthdate(employee.getBirthdate().toString());
        employeeForSOAP.setDepartmentId(employee.jsonGetDepartmentId());
        employeeForSOAP.setPositionId(employee.getPositionId());
        employeeForSOAP.setGradeId(employee.getGradeId());
        employeeForSOAP.setId(employee.getId());
        employeeForSOAP.setFired(employee.isFired());
        return employeeForSOAP;
    }
}
