package ru.tsconsulting.endpoints;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.tsconsulting.employee_ws.*;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.entities.Role;
import ru.tsconsulting.error_handling.not_found_exceptions.DepartmentNotFoundException;
import ru.tsconsulting.error_handling.not_specified_exceptions.DepartmentNotSpecifiedException;
import ru.tsconsulting.error_handling.not_found_exceptions.EmployeeNotFoundException;
import ru.tsconsulting.error_handling.not_found_exceptions.GradeNotFoundException;
import ru.tsconsulting.error_handling.notification_exceptions.InvalidSalaryValueException;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.EmployeeRepository;
import ru.tsconsulting.repositories.GradeRepository;
import ru.tsconsulting.repositories.PositionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Endpoint
public class EmployeeEndpoint {

    private static final String NAMESPACE_URI = "http://tsconsulting.ru/employee-ws";
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final GradeRepository gradeRepository;
    private final AuditReader auditReader;
    private final PositionRepository positionRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeEndpoint(EmployeeRepository employeeRepository,
                            DepartmentRepository departmentRepository,
                            AuditReader auditReader,
                            GradeRepository gradeRepository,
                            PositionRepository positionRepository, BCryptPasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.gradeRepository = gradeRepository;
        this.auditReader = auditReader;
        this.positionRepository = positionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "transferRequest")
    @ResponsePayload
    public TransferResponse transfer(@RequestPayload TransferRequest transferRequest) {
        Employee employee = employeeRepository.findByIdAndIsFiredIsFalse(transferRequest.getEmployeeId());
        if (employee == null) {
            throw new EmployeeNotFoundException(((Long) transferRequest.getEmployeeId()).toString());
        }
        Department newDepartment = departmentRepository.
                findByIdAndIsDismissedIsFalse(transferRequest.getNewDepartmentId());
        if (newDepartment == null) {
            throw new DepartmentNotFoundException(((Long) transferRequest.getNewDepartmentId()).toString());
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
            throw new EmployeeNotFoundException(((Long) firingRequest.getEmployeeId()).toString());
        }
        employee.setFired(true);
        employeeRepository.save(employee);
        return new FiringResponse();
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "employeeByDepRequest")
    @ResponsePayload
    public EmployeeByDepResponse employeeByDep(@RequestPayload EmployeeByDepRequest employeeByDepRequest) {
        Long departmentId = employeeByDepRequest.getDepartmentId();
        if (departmentRepository.findById(departmentId) == null) {
            throw new DepartmentNotFoundException(departmentId.toString());
        }
        EmployeeByDepResponse result = new EmployeeByDepResponse();
        List<EmployeeSOAP> employeeSOAPList = result.getEmployees();
        for (Employee iter : employeeRepository.findByDepartmentIdAndIsFiredIsFalseOrderByIdAsc(departmentId)) {
            employeeSOAPList.add(parseEmployee(iter));
        }
        return result;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "auditRequest")
    @ResponsePayload
    public AuditResponse getAudit(@RequestPayload AuditRequest auditRequest) {
        Long employeeId = auditRequest.getEmployeeId();
        if (employeeRepository.findById(employeeId) == null) {
            throw new EmployeeNotFoundException(employeeId.toString());
        }
        AuditResponse result = new AuditResponse();
        List<EmployeeSOAP> employeeSOAPList = result.getEmployees();
        AuditQuery query = auditReader.createQuery().forRevisionsOfEntity(Employee.class,
                true, false);
        query.add(AuditEntity.id().eq(employeeId));
        List<Employee> list = query.getResultList();
        for (Employee iter : list) {
            employeeSOAPList.add(parseEmployee(iter));
        }
        return result;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "employeeRequest")
    @ResponsePayload
    public EmployeeResponse getEmployee(@RequestPayload EmployeeRequest employeeRequest) {
        Long employeeId = employeeRequest.getEmployeeId();
        Employee employee = employeeRepository.findById(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException(employeeId.toString());
        }
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setEmployee(parseEmployee(employee));
        return employeeResponse;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createRequest")
    @ResponsePayload
    public CreateResponse createEmployee(@RequestPayload CreateRequest createRequest) {

        Employee employee = new Employee();
        Long grade = createRequest.getGradeId();
        if (grade != 0) {
            if (gradeRepository.findById(grade) == null) {
                throw new GradeNotFoundException(grade.toString());
            } else {
                employee.setGrade(gradeRepository.findById(grade));
            }
        }
        Long position = createRequest.getPositionId();
        if (position != 0) {
            if (positionRepository.findById(position) == null) {
                throw new GradeNotFoundException(position.toString());
            } else {
                employee.setPosition(positionRepository.findById(position));
            }
        }
        Long departmentId = createRequest.getDepartmentId();
        if (departmentId == 0) {
            throw new DepartmentNotSpecifiedException();
        } else {
            Department department = departmentRepository.findByIdAndIsDismissedIsFalse(departmentId);
            if (department == null) {
                throw new DepartmentNotFoundException(departmentId.toString());
            } else {
                employee.setDepartment(department);
            }
        }
        BigDecimal salary = createRequest.getSalary();
        if (salary != null) {
            if (!salary.toString().matches("\\d{0,17}[.]?\\d{0,2}")) {
                throw new InvalidSalaryValueException();
            }
        }
        employee.setFirstname(createRequest.getFirstname());
        employee.setMiddlename(createRequest.getMiddlename());
        employee.setLastname(createRequest.getLastname());
        employee.setGender(createRequest.getGender());
        employee.setSalary(createRequest.getSalary());
        employee.setBirthdate(LocalDate.parse(createRequest.getBirthdate()));
        employee.setUsername(createRequest.getUsername());
        System.err.println(createRequest.getPassword());
        employee.setPassword(passwordEncoder.encode(createRequest.getPassword()));
        employee.getRoles().add(Role.ROLE_USER);
        employee = employeeRepository.save(employee);
        CreateResponse createResponse = new CreateResponse();
        createResponse.setEmployee(parseEmployee(employee));
        return createResponse;
    }

    private EmployeeSOAP parseEmployee(Employee employee) {
        EmployeeSOAP employeeSOAP = new EmployeeSOAP();
        employeeSOAP.setFirstname(employee.getFirstname());
        employeeSOAP.setLastname(employee.getLastname());
        employeeSOAP.setMiddlename(employee.getMiddlename());
        if (employee.getBirthdate() != null) {
            employeeSOAP.setBirthdate(employee.getBirthdate().toString());
        }
        employeeSOAP.setDepartmentId(employee.jsonGetDepartmentId());
        if (employee.getPosition() != null) {
            employeeSOAP.setPositionId(employee.getPositionId());
        }
        if (employee.getGrade() != null) {
            employeeSOAP.setGradeId(employee.getGradeId());
        }
        if (employee.getGender() != null) {
            employeeSOAP.setGender(employee.getGender().toString());
        }
        employeeSOAP.setId(employee.getId());
        employeeSOAP.setFired(employee.isFired());
        employeeSOAP.setSalary(employee.getSalary());
        employeeSOAP.setUsername(employee.getUsername());;
        return employeeSOAP;
    }
}
