package ru.tsconsulting.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.tsconsulting.department_ws.*;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.errorHandling.DepartmentNotFoundException;
import ru.tsconsulting.repositories.DepartmentRepository;

import java.util.List;

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
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "subDepartmentsRequest")
    @ResponsePayload
    public SubDepartmentsResponse subDepartments(@RequestPayload SubDepartmentsRequest subDepartmentsRequest) {
        Long departmentId = subDepartmentsRequest.getDepartmentId();
        if (departmentRepository.findById(departmentId)==null)
        {
            throw new DepartmentNotFoundException(departmentId.toString());
        }
        SubDepartmentsResponse result = new SubDepartmentsResponse();
        List<DepartmentSOAP> departmentSOAPList =  result.getDepartments();
        for (Department iter:departmentRepository.findByParentIdAndIsDismissedIsFalse(departmentId))
        {
            departmentSOAPList.add(parseDepartment(iter));
        }
        return result;
    }

    private DepartmentSOAP parseDepartment(Department department) {
        DepartmentSOAP departmentSOAP = new DepartmentSOAP();
        departmentSOAP.setId(department.getId());
        departmentSOAP.setIsDismissed(department.isDismissed());
        departmentSOAP.setName(department.getName());
        return departmentSOAP;
    }
}
