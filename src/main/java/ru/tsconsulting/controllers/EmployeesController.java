package ru.tsconsulting.controllers;

import io.swagger.annotations.*;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.entities.Grade;
import ru.tsconsulting.entities.Position;
import ru.tsconsulting.errorHandling.*;
import ru.tsconsulting.errorHandling.not_found_exceptions.*;
import ru.tsconsulting.errorHandling.not_specified_exceptions.*;
import ru.tsconsulting.errorHandling.notification_exceptions.EmployeeIsAlreadyFiredException;
import ru.tsconsulting.errorHandling.notification_exceptions.EmployeeIsAlreadyHiredException;
import ru.tsconsulting.errorHandling.notification_exceptions.InvalidSalaryValueException;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.EmployeeRepository;
import ru.tsconsulting.repositories.GradeRepository;
import ru.tsconsulting.repositories.PositionRepository;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

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


    @ApiOperation(value = "Create employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful creation of employee"),
            @ApiResponse(code = 400, message = "Invalid attributes"),
            @ApiResponse(code = 404, message = "Department, position or grade does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(method = RequestMethod.POST)
    public Employee createEmployee(@ApiParam(value = "Data for creating a new employee")
                                   @Validated @RequestBody Employee.EmployeeDetails employeeDetails,
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
                throw new PositionNotFoundException(employeeDetails.getPosition().toString());
            } else {
                employee.setPosition(positionRepository.findById(employeeDetails.getPosition()));
            }
        }
        if (employeeDetails.getDepartment() == null) {
            throw new DepartmentNotSpecifiedException();
        } else {
            Department department = departmentRepository.findByIdAndIsDismissedIsFalse(employeeDetails.getDepartment());
            if (department == null) {
                throw new DepartmentNotFoundException(employeeDetails.getDepartment().toString());
            } else {
                employee.setDepartment(department);
            }
        }
        BigDecimal salary = employeeDetails.getSalary();
        if (salary!=null)
        {
            if (!salary.toString().matches("\\d{0,17}[.]?\\d{0,2}"))
            {
                throw new InvalidSalaryValueException();
            }
        }
        Employee result = employeeRepository.save(employee);
        return result;
    }

    @ApiOperation(value = "Return employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of employee"),
            @ApiResponse(code = 404, message = "Employee with given id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path = "/{employeeId}", method = RequestMethod.GET)
    public Employee getEmployee(@ApiParam(value = "Id of employee",
            required = true) @PathVariable Long employeeId,
                                HttpServletRequest request) {
        Employee employee = employeeRepository.findById(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException(employeeId.toString());
        }
        return employee;
    }

    @ApiOperation(value = "Edit employee", notes = "Currently supports editing of position, grade and salary")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful edition of employee"),
            @ApiResponse(code = 404, message = "Employee with given id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path = "/{employeeId}/edit", method = RequestMethod.POST)
    public Employee editEmployee(@ApiParam(value = "Id of employee",
            required = true) @PathVariable Long employeeId,
                                 @ApiParam(value = "Id of new position")
                                 @RequestParam(value = "newPositionId", required = false) Long newPositionId,
                                 @ApiParam(value = "Id of new grade")
                                 @RequestParam(value = "newGrade", required = false) Long newGrade,
                                 @ApiParam(value = "New salary")
                                 @RequestParam(value = "newSalary", required = false) BigDecimal newSalary,
                                 HttpServletRequest request) {
        Employee employee = employeeRepository.findById(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException(employeeId.toString());
        }
        if (newPositionId != null) {
            Position position = positionRepository.findById(newPositionId);
            if (position == null) {
                throw new PositionNotFoundException(newPositionId.toString());
            } else {
                employee.setPosition(position);
            }
        }
        if (newGrade != null) {
            Grade grade = gradeRepository.findById(newGrade);
            if (grade == null) {
                throw new GradeNotFoundException(newGrade.toString());
            } else {
                employee.setGrade(grade);
            }
        }
        if (newSalary != null) {
            employee.setSalary(newSalary);
        }
        employeeRepository.save(employee);
        return employee;
    }

    @ApiOperation(value = "Fire employee that was once created")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful firing of employee"),
            @ApiResponse(code = 404, message = "Employee with given id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path = "/{employeeId}", method = RequestMethod.DELETE)
    public void fireEmployee(@ApiParam(value = "Id of employee to be fired",
            required = true) @PathVariable Long employeeId,
                             HttpServletRequest request) {
        Employee employee = employeeRepository.findByIdAndIsFiredIsFalse(employeeId);
        if (employee != null) {
            employee.setFired(true);
            employeeRepository.save(employee);
        } else {
            throw new EmployeeIsAlreadyFiredException(employeeId.toString());
        }
    }

    @ApiOperation(value = "Rehire employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful hiring of employee"),
            @ApiResponse(code = 404, message = "Employee with given id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path = "/{employeeId}", method = RequestMethod.PUT)
    public void hireEmployee(@ApiParam(value = "Id of employee to be rehired",
            required = true) @PathVariable Long employeeId,
                             HttpServletRequest request) {
        Employee employee = employeeRepository.findByIdAndIsFiredIsTrue(employeeId);
        if (employee != null) {
            employee.setFired(false);
            employeeRepository.save(employee);
        } else {
            throw new EmployeeIsAlreadyHiredException(employeeId.toString());
        }
    }

    @ApiOperation(value = "Return employee by first name or last name")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of employee"),
            @ApiResponse(code = 400, message = "No attributes given"),
            @ApiResponse(code = 404, message = "Employee with given first name or last name  does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path = "/find", method = RequestMethod.GET)
    public List<Employee> findEmployeeByFirstAndLastName(@ApiParam("First name") @RequestParam(required = false)
                                                                 String firstName,
                                                         @ApiParam("Last name") @RequestParam(required = false)
                                                                 String lastName,
                                                         HttpServletRequest request) {
        List<Employee> employees;
        if (firstName != null && lastName != null) {
            employees = employeeRepository.findByFirstnameAndLastnameAndIsFiredFalse(firstName, lastName);
        } else if (firstName != null) {
            employees = employeeRepository.findByFirstname(firstName);
        } else if (lastName != null) {
            employees = employeeRepository.findByLastname(lastName);
        } else {
            throw new NoAttributesProvidedException();
        }
        return employees;
    }

    @ApiOperation(value = "Transfer employee from one department to another")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful transfer of employee"),
            @ApiResponse(code = 404, message = "Employee with given id does not exist or the department does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path = "/{employeeId}/transfer", method = RequestMethod.POST)
    public Employee transferEmployee(@ApiParam(value = "Id of employee",
            required = true) @PathVariable Long employeeId,
                                     @ApiParam(value = "Id of new department",
                                             required = true) @RequestParam(value = "newDepartmentId") Long newDepartmentId,
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

    @ApiOperation(value = "Return audit information of employee", notes = "Returns history of changes for an employee" +
            " by Id, currently without specifying the date of changes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of the audit information of employee"),
            @ApiResponse(code = 404, message = "Audit information for given employee does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path = "/{employeeId}/audit", method = RequestMethod.GET)
    public Map<String, Employee> getAudit(@ApiParam(value = "Id of employee",
            required = true) @PathVariable Long employeeId,
                                          @RequestParam(value = "from", required = false) String from,
                                          @RequestParam(value = "to", required = false) String to,
                                          HttpServletRequest request) {
        if (employeeRepository.findById(employeeId) == null) {
            throw new EmployeeNotFoundException(employeeId.toString());
        }
        ZoneId defaultZoneId = ZoneId.systemDefault();
        List<Number> revisions = auditReader.getRevisions(Employee.class, employeeId);
        Map<String, Employee> map = new HashMap<>();
        if (from != null && to != null) {
            LocalDateTime fromDate = LocalDateTime.parse(from);
            LocalDateTime toDate = LocalDateTime.parse(to);
            for (Number i : revisions) {
                Date date = auditReader.getRevisionDate(i);
                LocalDateTime localDateTime = date.toInstant().atZone(defaultZoneId).toLocalDateTime();
                if (localDateTime.isAfter(fromDate) && localDateTime.isBefore(toDate)) {
                    Employee employee = auditReader.find(Employee.class, employeeId, i);
                    map.put(localDateTime.toString(), employee);
                }
            }
        } else if (from != null) {
            LocalDateTime fromDate = LocalDateTime.parse(from);
            for (Number i : revisions) {
                Date date = auditReader.getRevisionDate(i);
                LocalDateTime localDateTime = date.toInstant().atZone(defaultZoneId).toLocalDateTime();
                if (localDateTime.isAfter(fromDate)) {
                    Employee employee = auditReader.find(Employee.class, employeeId, i);
                    map.put(localDateTime.toString(), employee);
                }
            }
        } else if (to != null) {
            LocalDateTime toDate = LocalDateTime.parse(to);
            for (Number i : revisions) {
                Date date = auditReader.getRevisionDate(i);
                LocalDateTime localDateTime = date.toInstant().atZone(defaultZoneId).toLocalDateTime();
                if (localDateTime.isBefore(toDate)) {
                    Employee employee = auditReader.find(Employee.class, employeeId, i);
                    map.put(localDateTime.toString(), employee);
                }
            }
        } else {
            for (Number i : revisions) {
                Date date = auditReader.getRevisionDate(i);
                LocalDateTime localDateTime = date.toInstant().atZone(defaultZoneId).toLocalDateTime();
                Employee employee = auditReader.find(Employee.class, employeeId, i);
                map.put(localDateTime.toString(), employee);
            }
        }
        return map;
    }


    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestError entityNotFound(EntityNotFoundException e) {
        return new RestError(Errors.ENTITY_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AttributeNotSpecifiedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestError attributeNotSpecified(AttributeNotSpecifiedException e) {
        return new RestError(Errors.ATTRIBUTE_NOT_SPECIFIED, e.getMessage());
    }

    @ExceptionHandler(EmployeeIsAlreadyFiredException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestError alreadyFired(EmployeeIsAlreadyFiredException e) {
        return new RestError(Errors.ALREADY_FIRED, e.getMessage());
    }

    @ExceptionHandler(EmployeeIsAlreadyHiredException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestError alreadyHired(EmployeeIsAlreadyHiredException e) {
        return new RestError(Errors.ALREADY_HIRED, e.getMessage());
    }
    @ExceptionHandler(InvalidSalaryValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestError invalidSalary(InvalidSalaryValueException e) {
        return new RestError(Errors.INVALID_ATTRIBUTE, e.getMessage());
    }
}
