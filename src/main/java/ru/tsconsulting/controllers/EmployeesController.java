package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.entities.Grade;
import ru.tsconsulting.entities.Position;
import ru.tsconsulting.errorHandling.*;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.EmployeeRepository;
import ru.tsconsulting.repositories.GradeRepository;
import ru.tsconsulting.repositories.PositionRepository;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeesController {
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final GradeRepository gradeRepository;
    private final DepartmentRepository departmentRepository;
    private final AuditReader auditReader;

    @Autowired
    public EmployeesController(EmployeeRepository employeeRepository,
                               PositionRepository positionRepository,
                               GradeRepository gradeRepository,
                               DepartmentRepository departmentRepository,
                               AuditReader auditReader) {
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.gradeRepository = gradeRepository;
        this.departmentRepository = departmentRepository;
        this.auditReader = auditReader;
    }

	@ApiOperation(value = "Transfer employee from one department to another")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful transfer of employee"),
			@ApiResponse(code = 404, message = "Employee with given id does not exist or one of the departments does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")}
	)
    @RequestMapping(path = "/{employeeId}/transfer", method = RequestMethod.POST)
    public Employee transferEmployee(@PathVariable Long employeeId,
                                     @RequestParam(value="newDepartmentId") Long newDepartmentId,
                                     HttpServletRequest request) {
        if (employeeRepository.findByIdAndIsFiredIsFalse(employeeId) == null) {
            throw new EmployeeNotFoundException(employeeId.toString());
        }

        if (departmentRepository.findByIdAndIsDismissedIsFalse(newDepartmentId) == null) {
            throw new DepartmentNotFoundException(newDepartmentId.toString());
        }

        Employee employee = employeeRepository.findByIdAndIsFiredIsFalse(employeeId);
        employee.setDepartment(departmentRepository.findByIdAndIsDismissedIsFalse(newDepartmentId));
        Employee result = employeeRepository.save(employee);

        return result;
    }


    @ApiOperation(value = "Return employee by first name and last name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of employee"),
            @ApiResponse(code = 404, message = "Employee with given first name or last name  does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path = "/find", method = RequestMethod.GET)
    public List<Employee> findEmployeeByFirstAndLastName(@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName,
                                HttpServletRequest request) {
        List<Employee> employees =  new ArrayList<>();


        if(firstName != null && lastName != null) {
            employees = employeeRepository.findByFirstnameAndLastnameAndIsFiredFalse(firstName, lastName);
        }
        else if(firstName != null) {
            employees = employeeRepository.findByFirstname(firstName);
        }
        else if(lastName != null) {
            employees = employeeRepository.findByLastname(lastName);
        }
        else {
            throw new InvalidParametersException();
        }

        return employees;
    }



	@ApiOperation(value = "Fire employee by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful firing of employee"),
			@ApiResponse(code = 404, message = "Employee with given id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")}
	)
    @RequestMapping(path = "/{employeeId}", method = RequestMethod.DELETE)
    public void fireEmployee(@PathVariable Long employeeId,
	                         HttpServletRequest request) {
        Employee employee = employeeRepository.findByIdAndIsFiredIsFalse(employeeId);
        if (employee != null) {
            employee.setFired(true);
            employeeRepository.save(employee);
        } else {
            throw new EmployeeNotFoundException(employeeId.toString());
        }
    }

	@ApiOperation(value = "Create employee by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful creation of employee"),
			@ApiResponse(code = 500, message = "Internal server error")}
	)
    @RequestMapping(method = RequestMethod.POST)
    public Employee createEmployee(@RequestBody Employee.EmployeeDetails employeeDetails,
                                   HttpServletRequest request) {
        Employee employee = new Employee(employeeDetails);

        if (employeeDetails.getGrade() != null) {
            if (gradeRepository.findById(employeeDetails.getGrade()) == null) {
                throw new GradeNotFoundException(employeeDetails.getGrade().toString());
            } else {
                employee.setGrade(gradeRepository.findById(employeeDetails.getGrade()));
            }
        }

        if (employeeDetails.getPosition() != null) {
            if (positionRepository.findById(employeeDetails.getPosition()) == null) {
                throw new GradeNotFoundException(employeeDetails.getPosition().toString());
            } else {
                employee.setPosition(positionRepository.findById(employeeDetails.getPosition()));
            }
        }

        if (employeeDetails.getDepartment() == null) {
            throw new DepartmentNotSpecifiedException(employeeDetails.getDepartment().toString());
        } else {
            Department department = departmentRepository.findByIdAndIsDismissedIsFalse(employeeDetails.getDepartment());
            if (department == null) {
                throw new DepartmentNotFoundException(employeeDetails.getDepartment().toString());
            } else {
                employee.setDepartment(department);
            }
        }
        Employee result = employeeRepository.save(employee);

        return result;
    }

	@ApiOperation(value = "Return employee by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful retrieval of employee"),
			@ApiResponse(code = 404, message = "Employee with given id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")}
	)
    @RequestMapping(path = "/{employeeId}", method = RequestMethod.GET)
    public Employee getEmployee(@PathVariable Long employeeId,
                                HttpServletRequest request) {
        Employee employee = employeeRepository.findById(employeeId);

        if (employee == null) {
            throw new EmployeeNotFoundException(employeeId.toString());
        }

        return employee;
    }

	@ApiOperation(value = "Return audit information of employee by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful retrieval of the audit information of employee"),
			@ApiResponse(code = 404, message = "Audit information for given employee does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")}
	)
    @RequestMapping(path="/{employeeId}/audit",method = RequestMethod.GET)
    public List<Employee> getAudit(@PathVariable Long employeeId,
                                     HttpServletRequest request) {
        if (employeeRepository.findById(employeeId) == null) {
            throw new EmployeeNotFoundException(employeeId.toString());
        }
        AuditQuery query = auditReader.createQuery().forRevisionsOfEntity(Employee.class,
                true, false);
        query.add(AuditEntity.id().eq(employeeId));
        List<Employee> list = query.getResultList();
        return list;
    }

	@ApiOperation(value = "Edit employee by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful edition of employee"),
			@ApiResponse(code = 404, message = "Employee with given id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")}
	)
    @RequestMapping(path="/{employeeId}/edit",method = RequestMethod.POST)
    public Employee editEmployee(@PathVariable Long employeeId,
                                 @RequestParam(value = "newPositionId", required=false) Long newPositionId,
                                 @RequestParam(value = "newGrade", required=false) Long newGrade,
                                 @RequestParam(value = "newSalary", required=false) Long newSalary,
                                             HttpServletRequest request) {
        Employee employee = employeeRepository.findByIdAndIsFiredIsFalse(employeeId);

        if (employee==null)
        {
            throw new EmployeeNotFoundException(employeeId.toString());
        }

        Position position = positionRepository.findById(newPositionId);

        if (position==null)
        {
            throw new PositionNotFoundException(employeeId.toString());
        }

        Grade grade = gradeRepository.findById(newGrade);

        if (grade==null)
        {
            throw new GradeNotFoundException(employeeId.toString());
        }

        employee.setPosition(position);
        employee.setGrade(grade);
        employee.setSalary(newSalary);
        employeeRepository.save(employee);

        return employee;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestError entityNotFound(EntityNotFoundException e) {
        return new RestError(Errors.ENTITY_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DepartmentNotSpecifiedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestError departmentNotSpecified(DepartmentNotSpecifiedException e) {
        return new RestError(Errors.DEPARTMENT_NOT_SPECIFIED, e.getMessage());
    }
}
