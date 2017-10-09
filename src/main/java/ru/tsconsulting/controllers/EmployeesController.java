package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hibernate.envers.AuditReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.entities.Grade;
import ru.tsconsulting.entities.Position;
import ru.tsconsulting.entities.Role;
import ru.tsconsulting.error_handling.RestStatus;
import ru.tsconsulting.error_handling.Status;
import ru.tsconsulting.error_handling.already_exist_exceptions.EmployeeIsAlreadyFiredException;
import ru.tsconsulting.error_handling.already_exist_exceptions.EmployeeIsAlreadyHiredException;
import ru.tsconsulting.error_handling.already_exist_exceptions.EntityAlreadyExistsException;
import ru.tsconsulting.error_handling.not_found_exceptions.*;
import ru.tsconsulting.error_handling.not_specified_exceptions.AttributeNotSpecifiedException;
import ru.tsconsulting.error_handling.not_specified_exceptions.NoAttributesProvidedException;
import ru.tsconsulting.error_handling.not_specified_exceptions.RolesNotSpecifiedException;
import ru.tsconsulting.error_handling.notification_exceptions.ParameterConstraintViolationException;
import ru.tsconsulting.error_handling.notification_exceptions.PasswordFormatException;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.EmployeeRepository;
import ru.tsconsulting.repositories.GradeRepository;
import ru.tsconsulting.repositories.PositionRepository;

import javax.persistence.PersistenceException;
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

    @CrossOrigin
    @ApiOperation(value = "Create employee",
            notes = "Employee has USER role by default")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful creation of employee"),
            @ApiResponse(code = 400, message = "Invalid attributes"),
            @ApiResponse(code = 404, message = "Department, position or grade does not exist"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 409, message = "Employee already exist")}
    )
    @RequestMapping(method = RequestMethod.POST)
    public Employee createEmployee(@RequestBody Employee employee,
                                   HttpServletRequest request) {

        return employeeRepository.save(employee);
    }

    @ApiOperation(value = "Return employee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of employee"),
            @ApiResponse(code = 404, message = "Employee with given id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path = "/{employeeId}", method = RequestMethod.GET)
    public Employee getEmployee(@ApiParam(value = "Id of employee, positive integer", required = true)
                                @PathVariable Long employeeId,
                                HttpServletRequest request) {
        Employee employee = employeeRepository.findById(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException(employeeId.toString());
        }
        return employee;
    }

    @ApiOperation(value = "Return all employees")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of employees"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(method = RequestMethod.GET)
    public List<Employee> getAllEmployees(HttpServletRequest request) {
        return employeeRepository.findAllByOrderByIdAsc();
    }

    @ApiOperation(value = "Edit employee", notes = "Currently supports editing of position, grade and salary")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful edition of employee"),
            @ApiResponse(code = 404, message = "Employee with given id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path = "/{employeeId}/edit", method = RequestMethod.POST)
    public Employee editEmployee(@ApiParam(value = "Id of employee, positive integer",
            required = true) @PathVariable Long employeeId,
                                 @ApiParam(value = "Id of new position, positive integer")
                                 @RequestParam(value = "newPositionId", required = false) Long newPositionId,
                                 @ApiParam(value = "Id of new grade positive integer")
                                 @RequestParam(value = "newGrade", required = false) Long newGrade,
                                 @ApiParam(value = "New salary, a non-negative decimal with precision = 14 and scale = 2")
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

    @ApiOperation(value = "Grant privileges to employee",
            notes = "Employee always has USER role. Can only be used by ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful edition of employee"),
            @ApiResponse(code = 404, message = "Employee with given id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path = "/{employeeId}/roles", method = RequestMethod.POST)
    public Employee grantPrivileges(@ApiParam(value = "Id of employee, positive integer",
            required = true) @PathVariable Long employeeId, String[] roles,
                                    HttpServletRequest request) {
        Employee employee = employeeRepository.findById(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException(employeeId.toString());
        }
        if (roles == null) {
            throw new RolesNotSpecifiedException();
        } else {
            for (String r : roles) {
                try {
                    Role role = Role.valueOf(r);
                    employee.getRoles().add(role);
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        return employeeRepository.save(employee);
    }

    @ApiOperation(value = "Set privileges of employee",
            notes = "Employee always has USER role, gets added automatically. Can only be used by ADMIN")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful edition of employee"),
            @ApiResponse(code = 404, message = "Employee with given id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path = "/{employeeId}/roles", method = RequestMethod.PUT)
    public Employee setPrivileges(@ApiParam(value = "Id of employee, positive integer",
            required = true) @PathVariable Long employeeId, String[] roles,
                                  HttpServletRequest request) {
        Employee employee = employeeRepository.findById(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException(employeeId.toString());
        }
        Set<Role> roleSet = new HashSet<>();
        if (roles != null) {
            for (String r : roles) {
                try {
                    Role role = Role.valueOf(r);
                    roleSet.add(role);
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        roleSet.add(Role.ROLE_USER);
        employee.setRoles(roleSet);
        return employeeRepository.save(employee);
    }

    @ApiOperation(value = "Fire employee that was once created")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful firing of employee"),
            @ApiResponse(code = 404, message = "Employee with given id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path = "/{employeeId}", method = RequestMethod.DELETE)
    public void fireEmployee(@ApiParam(value = "Id of employee to be fired, positive integer",
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
    public void hireEmployee(@ApiParam(value = "Id of employee to be rehired, positive integer",
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
            employees = employeeRepository.findByFirstnameAndLastname(firstName, lastName);
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
    public Employee transferEmployee(@ApiParam(value = "Id of employee, positive integer)",
            required = true) @PathVariable Long employeeId,
                                     @ApiParam(value = "Id of new department, positive integer",
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
        return employeeRepository.save(employee);
    }

    @ApiOperation(value = "Return audit information of employee", notes = "Returns history of changes for an employee" +
            " by Id, in descending order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of the audit information of employee"),
            @ApiResponse(code = 404, message = "Audit information for given employee does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path = "/{employeeId}/audit", method = RequestMethod.GET)
    public Map<LocalDateTime, Employee> getAudit(@ApiParam(value = "Id of employee, positive integer",
            required = true) @PathVariable Long employeeId,
                                                 @ApiParam(value = "Requires datetime, compliant to LocalDateTime format in Java, e.g. 2007-12-03T10:15:30")
                                                 @RequestParam(value = "from", required = false) String from,
                                                 @ApiParam(value = "Requires datetime, compliant to LocalDateTime format in Java, e.g. 2007-12-03T10:15:30")
                                                 @RequestParam(value = "to", required = false) String to,
                                                 HttpServletRequest request) {
        if (employeeRepository.findById(employeeId) == null) {
            throw new EmployeeNotFoundException(employeeId.toString());
        }
        ZoneId defaultZoneId = ZoneId.systemDefault();
        List<Number> revisions = auditReader.getRevisions(Employee.class, employeeId);
        Map<LocalDateTime, Employee> map = new TreeMap<>(Collections.reverseOrder());
        if (from != null && to != null) {
            LocalDateTime fromDate = LocalDateTime.parse(from);
            LocalDateTime toDate = LocalDateTime.parse(to);
            for (Number i : revisions) {
                Date date = auditReader.getRevisionDate(i);
                LocalDateTime localDateTime = date.toInstant().atZone(defaultZoneId).toLocalDateTime();
                if (localDateTime.isAfter(fromDate) && localDateTime.isBefore(toDate)) {
                    Employee employee = auditReader.find(Employee.class, employeeId, i);
                    map.put(localDateTime, employee);
                }
            }
        } else if (from != null) {
            LocalDateTime fromDate = LocalDateTime.parse(from);
            for (Number i : revisions) {
                Date date = auditReader.getRevisionDate(i);
                LocalDateTime localDateTime = date.toInstant().atZone(defaultZoneId).toLocalDateTime();
                if (localDateTime.isAfter(fromDate)) {
                    Employee employee = auditReader.find(Employee.class, employeeId, i);
                    map.put(localDateTime, employee);
                }
            }
        } else if (to != null) {
            LocalDateTime toDate = LocalDateTime.parse(to);
            for (Number i : revisions) {
                Date date = auditReader.getRevisionDate(i);
                LocalDateTime localDateTime = date.toInstant().atZone(defaultZoneId).toLocalDateTime();
                if (localDateTime.isBefore(toDate)) {
                    Employee employee = auditReader.find(Employee.class, employeeId, i);
                    map.put(localDateTime, employee);
                }
            }
        } else {
            for (Number i : revisions) {
                Date date = auditReader.getRevisionDate(i);
                LocalDateTime localDateTime = date.toInstant().atZone(defaultZoneId).toLocalDateTime();
                Employee employee = auditReader.find(Employee.class, employeeId, i);
                map.put(localDateTime, employee);
            }
        }
        return map;
    }


    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestStatus entityNotFound(EntityNotFoundException e) {
        return new RestStatus(Status.ENTITY_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AttributeNotSpecifiedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestStatus attributeNotSpecified(AttributeNotSpecifiedException e) {
        return new RestStatus(Status.ATTRIBUTE_NOT_SPECIFIED, e.getMessage());
    }

    @ExceptionHandler(EmployeeIsAlreadyFiredException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestStatus alreadyFired(EmployeeIsAlreadyFiredException e) {
        return new RestStatus(Status.ALREADY_FIRED, e.getMessage());
    }

    @ExceptionHandler(EmployeeIsAlreadyHiredException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestStatus alreadyHired(EmployeeIsAlreadyHiredException e) {
        return new RestStatus(Status.ALREADY_HIRED, e.getMessage());
    }


    @ExceptionHandler(PasswordFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestStatus invalidPassword(PasswordFormatException e) {
        return new RestStatus(Status.INVALID_ATTRIBUTE, e.getMessage());
    }

    @ExceptionHandler(ParameterConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestStatus invalidUsername(ParameterConstraintViolationException e) {
        return new RestStatus(Status.INVALID_ATTRIBUTE, e.getMessage());
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public RestStatus conflict(EntityAlreadyExistsException e) {
        return new RestStatus(Status.ALREADY_EXISTS, e.getMessage());
    }

    @ExceptionHandler(PersistenceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public RestStatus excepton(PersistenceException e) {
        return new RestStatus(Status.INVALID_ATTRIBUTE, e.getCause().getCause().getMessage());
    }

}
