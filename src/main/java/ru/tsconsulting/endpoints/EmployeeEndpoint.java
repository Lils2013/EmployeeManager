package ru.tsconsulting.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.tsconsulting.employee_ws.TransferRequest;
import ru.tsconsulting.employee_ws.EmployeeForSOAP;
import ru.tsconsulting.employee_ws.TransferResponse;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
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
        Department newDepartment = departmentRepository.
                findByIdAndIsDismissedIsFalse(transferRequest.getNewDepartmentId());
        employee.setDepartment(newDepartment);
        employeeRepository.save(employee);
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setEmployee(parseEmployee(employee));
        return transferResponse;
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
