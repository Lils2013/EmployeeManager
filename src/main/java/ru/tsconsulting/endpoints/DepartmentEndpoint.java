package ru.tsconsulting.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.tsconsulting.department_ws.DepartmentSOAP;
import ru.tsconsulting.department_ws.GetRequest;
import ru.tsconsulting.department_ws.GetResponse;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.repositories.DepartmentRepository;

@Endpoint
public class DepartmentEndpoint {
    private static final String NAMESPACE_URI = "http://tsconsulting.ru/department-ws";
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentEndpoint(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getRequest")
    @ResponsePayload
    public GetResponse getDepartment(@RequestPayload GetRequest getRequest) {
        Department department = departmentRepository.findByIdAndIsDismissedIsFalse(getRequest.getDepartmentId());
        GetResponse getResponse = new GetResponse();
        getResponse.setDepartment(parseDepartment(department));
        return getResponse;
    }

    private DepartmentSOAP parseDepartment(Department department) {
        DepartmentSOAP departmentSOAP = new DepartmentSOAP();
        departmentSOAP.setId(department.getId());
        departmentSOAP.setIsDismissed(department.isDismissed());
        departmentSOAP.setName(department.getName());
        return departmentSOAP;
    }
}
